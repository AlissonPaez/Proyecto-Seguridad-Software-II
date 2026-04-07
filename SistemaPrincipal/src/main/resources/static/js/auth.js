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
    
    if (!nombre || !clave) {
        res.classList.remove('hidden');
        res.innerText = "Por favor, llene todos los campos.";
        return;
    }

    try {
        const response = await fetch('/auth/registrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombreUsuario: nombre, contraseña: clave })
        });
        const data = await response.text();
        res.classList.remove('hidden');
        res.innerText = data;
        // Limpiar campos si el registro fue exitoso
        if (response.ok) {
            document.getElementById('regNombre').value = '';
            document.getElementById('regClave').value = '';
        }
    } catch (error) {
        res.classList.remove('hidden');
        res.innerText = `Error: ${error.message}`;
    }
}

async function activarMFA() {
    const nombre = document.getElementById('mfaNombre').value;
    const res = document.getElementById('resMFA');
    
    if (!nombre) {
        res.classList.remove('hidden');
        res.innerText = "Por favor, ingresa tu usuario.";
        return;
    }

    try {
        const response = await fetch(`/auth/activar-mfa?nombre=${encodeURIComponent(nombre)}`);
        const data = await response.json();
        res.classList.remove('hidden');
        if (data.secreto) {
            res.innerHTML = `<strong>Secreto:</strong> <code style="color:red;">${data.secreto}</code><br><small>Escanea este código en Google Authenticator</small>`;
        } else {
            res.innerText = `Error: ${data.mensaje || 'No se pudo generar el código'}`;
        }
    } catch (error) {
        res.classList.remove('hidden');
        res.innerText = `Error: ${error.message}`;
    }
}

async function login() {
    const nombre = document.getElementById('logNombre').value;
    const clave = document.getElementById('logClave').value;
    const codigo = parseInt(document.getElementById('logCodigo').value);
    const res = document.getElementById('resLogin');

    if (!nombre || !clave || !codigo) {
        res.classList.remove('hidden');
        res.innerText = "Por favor, llene todos los campos.";
        return; 
    }

    try {
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombre, clave, codigo })
        });
        const data = await response.text();
        res.classList.remove('hidden');
        res.innerText = data;
    } catch (error) {
        res.classList.remove('hidden');
        res.innerText = `Error: ${error.message}`;
    }
}