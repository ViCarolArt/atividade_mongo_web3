const apiUrl = "/usuarios"; // URL relativa: mesma origem (sem host fixo, sem CORS)
const form = document.getElementById("userForm");
const table = document.getElementById("userTable");
const msg = document.getElementById("msg");
const btnAnterior = document.getElementById("btnAnterior");
const btnProxima = document.getElementById("btnProxima");
const paginaInfo = document.getElementById("paginaInfo");
const TAMANHO = 20;
let paginaAtual = 0;
let totalPaginas = 0;

function mostrarErro(texto) { msg.textContent = texto || ""; }

function lerCookie(nome) {
  const par = document.cookie.split("; ").find(c => c.startsWith(nome + "="));
  return par ? decodeURIComponent(par.split("=")[1]) : null;
}

// Todas as chamadas passam por aqui: envia o token CSRF e trata erros sem expor detalhes
async function api(caminho, opcoes = {}) {
  const metodo = (opcoes.method || "GET").toUpperCase();
  const headers = { ...(opcoes.headers || {}) };
  if (metodo !== "GET") {
    const token = lerCookie("XSRF-TOKEN");
    if (token) headers["X-XSRF-TOKEN"] = token;
  }
  const res = await fetch(apiUrl + caminho, { ...opcoes, headers, credentials: "same-origin" });
  if (res.status === 401) throw new Error("Sessão expirada. Recarregue a página e faça login.");
  if (res.status === 403) throw new Error("Você não tem permissão para esta ação.");
  if (!res.ok) {
    let detalhe = "Não foi possível concluir a operação.";
    try {
      const corpo = await res.json();
      if (corpo.campos) detalhe = Object.values(corpo.campos).join(" ");
      else if (corpo.detail) detalhe = corpo.detail;
    } catch (_) { /* corpo vazio */ }
    throw new Error(detalhe);
  }
  return res.status === 204 ? null : res.json();
}

// Renderização segura: textContent nunca interpreta HTML (evita XSS)
function criarLinha(u) {
  const tr = document.createElement("tr");

  const tdNome = document.createElement("td");
  tdNome.textContent = u.nome;
  const tdEmail = document.createElement("td");
  tdEmail.textContent = u.email;

  const tdAcoes = document.createElement("td");
  tdAcoes.className = "actions";

  const btnEditar = document.createElement("button");
  btnEditar.type = "button";
  btnEditar.textContent = "Editar";
  btnEditar.addEventListener("click", () => editarUsuario(u));

  const btnExcluir = document.createElement("button");
  btnExcluir.type = "button";
  btnExcluir.className = "delete";
  btnExcluir.textContent = "Excluir";
  btnExcluir.addEventListener("click", () => deletarUsuario(u.id));

  tdAcoes.append(btnEditar, btnExcluir);
  tr.append(tdNome, tdEmail, tdAcoes);
  return tr;
}

async function carregarUsuarios() {
  try {
    const dados = await api(`?page=${paginaAtual}&size=${TAMANHO}`);
    totalPaginas = dados.totalPaginas;
    table.replaceChildren(...dados.conteudo.map(criarLinha));
    paginaInfo.textContent = `Página ${totalPaginas === 0 ? 0 : dados.pagina + 1} de ${totalPaginas}`;
    btnAnterior.disabled = paginaAtual <= 0;
    btnProxima.disabled = paginaAtual + 1 >= totalPaginas;
    mostrarErro("");
  } catch (e) {
    mostrarErro(e.message);
  }
}

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("userId").value;
  const usuario = {
    nome: document.getElementById("nome").value.trim(),
    email: document.getElementById("email").value.trim()
  };
  if (!usuario.nome || !usuario.email) { mostrarErro("Preencha nome e e-mail."); return; }

  try {
    await api(id ? `/${encodeURIComponent(id)}` : "", {
      method: id ? "PUT" : "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(usuario)
    });
    novoUsuario();
    carregarUsuarios();
  } catch (err) {
    mostrarErro(err.message);
  }
});

function editarUsuario(u) {
  document.getElementById("userId").value = u.id;
  document.getElementById("nome").value = u.nome;
  document.getElementById("email").value = u.email;
}

function novoUsuario() {
  form.reset();
  document.getElementById("userId").value = "";
  mostrarErro("");
}

async function deletarUsuario(id) {
  if (!confirm("Deseja excluir este usuário?")) return;
  try {
    await api(`/${encodeURIComponent(id)}`, { method: "DELETE" });
    if (table.children.length === 1 && paginaAtual > 0) paginaAtual--;
    carregarUsuarios();
  } catch (err) {
    mostrarErro(err.message);
  }
}

document.getElementById("btnNovo").addEventListener("click", novoUsuario);
btnAnterior.addEventListener("click", () => { if (paginaAtual > 0) { paginaAtual--; carregarUsuarios(); } });
btnProxima.addEventListener("click", () => { if (paginaAtual + 1 < totalPaginas) { paginaAtual++; carregarUsuarios(); } });

carregarUsuarios();
