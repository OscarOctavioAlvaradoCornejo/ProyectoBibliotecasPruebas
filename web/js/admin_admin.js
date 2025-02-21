window.onload = () => {
    cargarUsuarios(); // Llamar a la función para cargar libros al cargar la página
};

// Función para cargar usuarios
function cargarUsuarios() {
    let ruta = "http://192.168.1.5:8080/ProyectoBibliotecasPruebas/api/usuarios/getAllUsuarios"; // Cambia la ruta según tu API
    fetch(ruta)
        .then(response => response.json())
        .then(data => {
            console.log(data); // Mostrar los datos en la consola
            const usuariosTableBody = document.getElementById("usuariosTableBody");
            usuariosTableBody.innerHTML = ""; // Limpiar el cuerpo de la tabla
            
            data.forEach(usuario => {
                const row = `
                    <tr>
                        <td>${usuario.username}</td>
                        <td>${usuario.estatus === 1 ? 'Activo' : 'Inactivo'}</td>
                        <td>${usuario.tipo === '1' ? 'Administrador' : (usuario.tipo === '2' ? 'Bibliotecario' : 'Alumno')}</td>
                    </tr>`;
                usuariosTableBody.innerHTML += row; // Agregar cada fila a la tabla
            });
        })
        .catch(error => console.error('Error al cargar usuarios:', error));
}
function insertarUsuario() {
    let ruta = "https://980c-2806-264-5485-b55-5988-64e3-1a27-75a8.ngrok-free.app/ProyectoBibliotecasPruebas/api/usuarios/agregarUsuarios"; // Cambia la ruta según tu API
    
    let username = document.getElementById("username").value;
    let pass = document.getElementById("pass").value;
    let tipo = document.getElementById("tipo").value;

    // Validación inicial de campos
    if (!username || !pass || !tipo) {
        Swal.fire('Error', 'Todos los campos son obligatorios', 'error');
        return;
    }

    const usuarioData = {
        username: username,
        pass: pass,
        estatus: "1", // Asumimos que el estatus es 1
        tipo: tipo // Tipo seleccionado del formulario
    };

    fetch(ruta, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuarioData)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                throw new Error(data.message || 'Error desconocido'); // Aquí capturamos el mensaje de error
            });
        }
        return response.json();
    })
    .then(data => {
        console.log(data); // Verifica la respuesta
        if(data.idUsuario === -1){
            Swal.fire({
                title: 'Error!',
                text: 'El nombre de usuario debe tener entre 5 y 100 caracteres.',
                icon: 'error',
                confirmButtonText: 'Aceptar'
            });
        } else {
            // Mostrar mensaje de éxito
            Swal.fire({
                title: 'Éxito!',
                text: 'Usuario: '+ data.username + ' insertado correctamente ',
                icon: 'success',
                confirmButtonText: 'Aceptar'
            });
            
            cargarUsuarios();  // Cargar de nuevo la lista de usuarios
            // Opcional: Puedes limpiar los campos del formulario aquí
            document.getElementById("username").value = '';
            document.getElementById("pass").value = '';
            document.getElementById("tipo").value = '#'; // Limpia el select
        }
    })
    .catch(error => {
        console.error('Error al insertar usuario:', error);
        // Mostrar mensaje de error
        Swal.fire({
            title: 'Error!',
            text: 'Error al insertar usuario: ' + error.message,
            icon: 'error',
            confirmButtonText: 'Aceptar'
        });
    });
}
