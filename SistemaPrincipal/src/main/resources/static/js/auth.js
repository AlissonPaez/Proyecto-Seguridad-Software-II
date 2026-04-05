function showView(viewId) {
    document.getElementById('view-login').classList.add('hidden');
    document.getElementById('view-register').classList.add('hidden');
    document.getElementById('view-mfa').classList.add('hidden');
    document.getElementById(viewId).classList.remove('hidden');
}

async function registrar() {
    const nombre = document.getElementById('regNombre').value;
    const clave = document.getElementById('regClave').value;
    const res = document.getElementById('resRegistro');
    
    const response = await fetch('/auth/registrar', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ nombreUsuario: nombre, contraseña: clave })
    });
    const data = await response.text();
    res.classList.remove('hidden');
    res.innerText = data;
}

async function activarMFA() {
    const nombre = document.getElementById('mfaNombre').value;
    const res = document.getElementById('resMFA');
    const response = await fetch(`/auth/activar-mfa?nombre=${nombre}`);
    const data = await response.json();
    res.classList.remove('hidden');
    res.innerHTML = `<strong>Secreto:</strong> <code style="color:red;">${data.secreto}</code>`;
}

async function login() {
    const nombre = document.getElementById('logNombre').value;
    const clave = document.getElementById('logClave').value;
    const codigo = document.getElementById('logCodigo').value;
    const res = document.getElementById('resLogin');
    
    const response = await fetch(`/auth/login?nombre=${nombre}&clave=${clave}&codigo=${codigo}`);
    const data = await response.text();
    res.classList.remove('hidden');
    res.innerText = data;
}