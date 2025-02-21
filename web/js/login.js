document.getElementById("loginForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Evitar el envío del formulario

    // Obtener los valores de usuario y contraseña
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    const loginData = {
        username: username,
        pass: password
    };

    fetch("https://980c-2806-264-5485-b55-5988-64e3-1a27-75a8.ngrok-free.app/ProyectoBibliotecasPruebas/api/user/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(loginData)
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Credenciales incorrectas");
        }
        return response.json();
    })
    .then(data => {
        // Mostrar mensaje de éxito
        Swal.fire('Éxito', data.mensaje, 'success');

        // Redirigir según el tipo de usuario
        switch (data.tipo) {
            case "1": // Administrador
                window.location.href = 'vistaAdmin.html';
                break;
            case "2": // Bibliotecario
                window.location.href = 'bibliotecario.html';
                break;
            case "3": // Alumno
                window.location.href = 'vistaAlumno.html';
                break;
            default:
                Swal.fire('Error', 'Tipo de usuario desconocido', 'error');
        }
    })
    .catch(error => {
        Swal.fire('Error', error.message, 'error'); // Mostrar el error
    });
});
