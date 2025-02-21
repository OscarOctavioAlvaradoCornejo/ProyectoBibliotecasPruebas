window.onload = () => {
    cargarLibrosPublico(); // Llamar a la función para cargar libros al cargar la página
};


// Definición de la clase LibroViewModel
class LibroViewModel {
    constructor(idLibro, nombreLibro, autor, genero, estatus, archivoBase64) {
        this.idLibro = idLibro;
        this.nombreLibro = nombreLibro;
        this.autor = autor;
        this.genero = genero;
        this.estatus = estatus;
        this.archivoBase64 = archivoBase64;
    }
}

function cargarLibrosPublico() {
    let ruta = "https://980c-2806-264-5485-b55-5988-64e3-1a27-75a8.ngrok-free.app/ProyectoBibliotecasPruebas/api/libro/getPrivado";
    fetch(ruta)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la red: ' + response.status + ' ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            const librosTableBody = document.getElementById("librosTableBody");
            librosTableBody.innerHTML = "";

            data.forEach(libro => {
                let pdfUrl;

                // Verificar si el archivo es remoto (termina en .pdf)
                if (libro.archivoPdf && libro.archivoPdf.trim().endsWith('.pdf')) {
                    // Construir la URL para archivos remotos
                    pdfUrl = `http://192.168.131.147:5500/Libros/${libro.archivoPdf}`;
                } else if (libro.archivoPdf && libro.archivoPdf.trim() !== "") {
                    // Si es un archivo en base64
                    pdfUrl = libro.archivoPdf.startsWith('data:application/pdf;base64,') 
                        ? libro.archivoPdf 
                        : `data:application/pdf;base64,${libro.archivoPdf}`;
                } else {
                    console.warn('El libro no contiene un archivo PDF válido:', libro);
                    pdfUrl = ''; // Manejar la falta de archivo
                }

                const row = `
                    <tr>
                        <td>${sanitizar(libro.nombreDelLibro)}</td>
                        <td>${sanitizar(libro.nombreDelAutor)}</td>
                        <td>${sanitizar(libro.categoria)}</td>
                        <td>
                            <button onclick="cargarDatosLibro(${libro.identificador})">Editar</button>
                        </td>
                        <td>
                            ${pdfUrl ? `<button onclick="abrirPDF('${pdfUrl}')">Ver PDF</button>` : 'No disponible'}
                        </td>
                    </tr>`;
                librosTableBody.innerHTML += row;
            });
        })
        .catch(error => console.error('Error al cargar libros:', error));
}



// Función para cargar libros



// Función para abrir el PDF en un modal
function abrirPDF(pdfUrl) {
    const embed = document.getElementById('pdfEmbed');
    embed.src = pdfUrl; // Establecer la fuente del PDF
    const modal = new bootstrap.Modal(document.getElementById('pdfModal'));
    modal.show(); // Mostrar el modal
}

function insertarLibro() {
    let ruta = "https://980c-2806-264-5485-b55-5988-64e3-1a27-75a8.ngrok-free.app/ProyectoBibliotecasPruebas/api/libro/insert";
    
    let nombreLibro = document.getElementById("nombreLibro").value;
    let autor = document.getElementById("autor").value;
    let genero = document.getElementById("genero").value;
    let archivoInput = document.getElementById("archivo").files[0];

    // Validación inicial de campos
    if (!nombreLibro || !autor || !genero || !archivoInput) {
        Swal.fire('Error', 'Todos los campos son obligatorios, incluido el archivo', 'error');
        return;
    }

    let reader = new FileReader();
    reader.onload = function(event) {
        let archivoBase64 = event.target.result.split(',')[1];

        const libroData = {
            nombreLibro: nombreLibro,
            autor: autor,
            genero: genero,
            estatus: "1", // Asumimos que el estatus es 1
            archivoBase64: archivoBase64 // Asegúrate de enviar este campo
        };

        fetch(ruta, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(libroData)
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data); // Aquí capturamos el mensaje de error
                });
            }
            return response.json();
        })
        .then(data => {
            console.log(data); // Verifica la respuesta
            if(data.idLibro == -1){
                            Swal.fire({
                title: 'Error!',
                text: 'El nombre del libro y la categoría debe tener entre 5 y 100 caracteres.',
                icon: 'error',
                confirmButtonText: 'Aceptar'
            });
            }else{
            // Mostrar mensaje de éxito
            Swal.fire({
                title: 'Éxito!',
                text: 'Libro: '+ data.nombreLibro + ' insertado correctamente ',
                icon: 'success',
                confirmButtonText: 'Aceptar'
            });
            
            cargarLibrosPublico();  
            // Opcional: Puedes limpiar los campos del formulario aquí
            document.getElementById("nombreLibro").value = '';
            document.getElementById("autor").value = '';
            document.getElementById("genero").value = '';
            document.getElementById("archivo").value = ''; // Limpia el input de archivo
        }
        })
        .catch(error => {
            console.error('Error al insertar libro:', error);
            // Mostrar mensaje de error
            Swal.fire({
                title: 'Error!',
                text: 'El nombre del libro y la categoría debe tener entre 5 y 100 caracteres.',
                icon: 'error',
                confirmButtonText: 'Aceptar'
            });
        });
    };

    reader.readAsDataURL(archivoInput); // Asegúrate de que estás leyendo el archivo como Data URL
}



// Función para cargar datos del libro al editar
function cargarDatosLibro(idLibro) {
    let ruta = `https://980c-2806-264-5485-b55-5988-64e3-1a27-75a8.ngrok-free.app/ProyectoBibliotecasPruebas/api/libro/getById`;
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ id_libro: idLibro })
    };

    fetch(ruta, requestOptions)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar libro');
            }
            return response.json();
        })
        .then(libro => {
            document.getElementById("idLibro").value = libro.idLibro;  // Cargar el ID en el campo oculto
            document.getElementById("nombreLibro").value = libro.nombreLibro;
            document.getElementById("autor").value = libro.autor;
            document.getElementById("genero").value = libro.genero;
        })
        .catch(error => console.error('Error al cargar libro:', error));
}

// Función para actualizar el libro
function actualizarLibro() {
    let ruta = "https://980c-2806-264-5485-b55-5988-64e3-1a27-75a8.ngrok-free.app/ProyectoBibliotecasPruebas/api/libro/update";

    // Obtener los valores del formulario
    let idLibro = document.getElementById("idLibro").value;  // ID oculto
    let nombreLibro = document.getElementById("nombreLibro").value;
    let autor = document.getElementById("autor").value;
    let genero = document.getElementById("genero").value;
    let archivoInput = document.getElementById("archivo").files[0];  // Obtener el archivo

    // Validar que todos los campos estén llenos
    if (!idLibro || !nombreLibro || !autor || !genero || !archivoInput) {
        Swal.fire('Error', 'Todos los campos son obligatorios, incluido el archivo', 'error');
        return;
    }

    // Leer el archivo y convertirlo a Base64
    let reader = new FileReader();
    reader.onload = function(event) {
        let archivoBase64 = event.target.result.split(',')[1];  // Obtener el contenido en Base64

        // Construir el objeto libro
        const libroData = {
            idLibro: idLibro,
            nombreLibro: nombreLibro,
            autor: autor,
            genero: genero,
            estatus: 1,  // Asumimos que el estatus es 1 (Disponible)
            archivoBase64: archivoBase64
        };

        // Configuración del request
        const requestOptions = {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(libroData)
        };

        // Enviar la solicitud de actualización
        fetch(ruta, requestOptions)
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message); // Usar el mensaje de error del servidor
                    });
                }
                return response.json();
            })
            .then(json => {
                Swal.fire('Éxito', `${json.mensaje}`, 'success');
                document.getElementById("formLibro").reset();  // Limpiar el formulario
                cargarLibrosPublico();  // Recargar la lista de libros
            })
            .catch(error => {
                Swal.fire('Error', 'El nombre del libro y la categoría debe tener entre 5 y 100 caracteres. ', 'error');
            });
    };

    // Leer el archivo como Data URL (Base64)
    reader.readAsDataURL(archivoInput);  
}


function sanitizar(texto) {
    if (!texto) return ''; // Retornar vacío si el texto es nulo o indefinido
    return texto
        .replace(/&/g, '&amp;') // Reemplazar & por &amp;
        .replace(/</g, '&lt;')  // Reemplazar < por &lt;
        .replace(/>/g, '&gt;')  // Reemplazar > por &gt;
        .replace(/"/g, '&quot;') // Reemplazar " por &quot;
        .replace(/'/g, '&#39;'); // Reemplazar ' por &#39;
}

/*
const express = require('express');
const cors = require('cors')
const app = express ()
const port = 5500

app.use(cors())

app.get('/', (req, res)=>{
    res.send({status: 'Bien'})
})

app.get('/test', (req, res)=>{
    res.send({status: 'Bien'})
})

app.listen(port, ()=>{
    console.log(`Example app listening at http://localhost:${port}`)
})*/