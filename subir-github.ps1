# =============================================================================
#  EcoCity ESG - Publica o projeto no GitHub e dispara o pipeline CI/CD
#  Uso:  clique com o botao direito > "Executar com o PowerShell"
#        ou, no terminal:  .\subir-github.ps1
# =============================================================================

$ErrorActionPreference = "Stop"
$NOME_REPO = "ecocity-esg-devops"

function Passo($txt) { Write-Host "`n>> $txt" -ForegroundColor Cyan }
function Ok($txt)    { Write-Host "   [ok] $txt" -ForegroundColor Green }
function Erro($txt)  { Write-Host "`n[ERRO] $txt" -ForegroundColor Red }

# --- roda sempre a partir da pasta onde o script esta -----------------------
Set-Location -Path $PSScriptRoot

# --- 1. confere se estamos na pasta certa -----------------------------------
Passo "Verificando a pasta do projeto"
if (-not (Test-Path ".\docker-compose.yml") -or -not (Test-Path ".\.github\workflows\ci-cd.yml")) {
    Erro "Este script precisa estar DENTRO da pasta 'ecocity-esg-devops-completo'."
    Write-Host "   Mova o arquivo para la (junto do docker-compose.yml) e rode de novo."
    Read-Host "`nEnter para sair"; exit 1
}
Ok "docker-compose.yml e .github/workflows/ci-cd.yml encontrados"

# --- 2. confere o git --------------------------------------------------------
Passo "Verificando o Git"
if (-not (Get-Command git -ErrorAction SilentlyContinue)) {
    Erro "Git nao encontrado. Instale em https://git-scm.com/download/win e rode de novo."
    Read-Host "`nEnter para sair"; exit 1
}
Ok (git --version)

if (-not (git config user.email)) {
    $email = Read-Host "   Seu e-mail do GitHub"
    $nome  = Read-Host "   Seu nome"
    git config --global user.email "$email"
    git config --global user.name  "$nome"
    Ok "Identidade do Git configurada"
}

# --- 3. commit ---------------------------------------------------------------
Passo "Preparando o repositorio local"
if (-not (Test-Path ".git")) { git init | Out-Null; Ok "Repositorio inicializado" }
else { Ok "Repositorio ja existia" }

git add .
$temAlteracao = git status --porcelain
if ($temAlteracao) {
    git commit -m "EcoCity ESG - Desafio DevOps (CI/CD, Docker e Docker Compose)" | Out-Null
    Ok "Commit criado"
} else {
    Ok "Nada novo para commitar"
}
git branch -M main
Ok "Branch main definida"

# --- 4. publica no GitHub ----------------------------------------------------
Passo "Publicando no GitHub"
$remoto = git remote get-url origin 2>$null

if (-not $remoto) {
    $temGh = Get-Command gh -ErrorAction SilentlyContinue
    $ghAutenticado = $false
    if ($temGh) { gh auth status 2>$null | Out-Null; $ghAutenticado = ($LASTEXITCODE -eq 0) }

    if ($ghAutenticado) {
        Ok "GitHub CLI autenticado - criando o repositorio '$NOME_REPO' como publico"
        gh repo create $NOME_REPO --public --source=. --remote=origin --push
        if ($LASTEXITCODE -ne 0) { Erro "Falha ao criar o repositorio pelo gh."; Read-Host "`nEnter para sair"; exit 1 }
    } else {
        Write-Host ""
        Write-Host "   O GitHub CLI nao esta disponivel/autenticado." -ForegroundColor Yellow
        Write-Host "   1) Abra https://github.com/new"
        Write-Host "   2) Nome sugerido: $NOME_REPO   |   visibilidade: Public"
        Write-Host "   3) NAO marque README, .gitignore nem licenca"
        Write-Host "   4) Crie e copie a URL (ex: https://github.com/usuario/$NOME_REPO.git)"
        Write-Host ""
        $url = Read-Host "   Cole a URL do repositorio"
        if (-not $url) { Erro "URL vazia."; Read-Host "`nEnter para sair"; exit 1 }
        git remote add origin $url.Trim()
        git push -u origin main
        if ($LASTEXITCODE -ne 0) { Erro "Falha no push. Confira a URL e suas credenciais do GitHub."; Read-Host "`nEnter para sair"; exit 1 }
    }
} else {
    Ok "Remoto ja configurado: $remoto"
    git push -u origin main
    if ($LASTEXITCODE -ne 0) { Erro "Falha no push."; Read-Host "`nEnter para sair"; exit 1 }
}
Ok "Push concluido - o pipeline ja foi disparado"

# --- 5. abre a aba Actions ---------------------------------------------------
Passo "Abrindo a aba Actions no navegador"
$origem = (git remote get-url origin) -replace "\.git$", "" -replace "^git@github\.com:", "https://github.com/"
Start-Process "$origem/actions"

Write-Host ""
Write-Host "=======================================================================" -ForegroundColor Green
Write-Host " PRONTO. Agora e so acompanhar." -ForegroundColor Green
Write-Host "=======================================================================" -ForegroundColor Green
Write-Host ""
Write-Host " Abra o run mais recente e espere os 3 jobs ficarem verdes:"
Write-Host "   1. Build & Test                  (~2 a 4 min)"
Write-Host "   2. Deploy - Ambiente Staging     (~5 a 8 min)"
Write-Host "   3. Deploy - Ambiente Producao    (~5 a 8 min)"
Write-Host ""
Write-Host " O PRINT que falta na documentacao:" -ForegroundColor Yellow
Write-Host "   a tela do run aberto, mostrando os 3 jobs encadeados com o check verde."
Write-Host ""
Write-Host " Repositorio: $origem"
Write-Host ""
Read-Host "Enter para fechar"
