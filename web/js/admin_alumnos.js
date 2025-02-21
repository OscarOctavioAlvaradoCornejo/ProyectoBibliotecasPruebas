let libros = []; // Variable para almacenar los libros cargados

window.onload = () => {
    cargarLibrosPublico(); // Llamar a la función para cargar libros al cargar la página
};



function mostrarLibros(librosData) {
    const librosTableBody = document.getElementById("librosTableBody");
    librosTableBody.innerHTML = ""; // Limpiar el cuerpo de la tabla

    // Iterar sobre los datos recibidos y añadirlos a la tabla
    librosData.forEach(libro => {
        const pdfUrl = `data:application/pdf;base64,${libro.archivoPdf}`;
        const row = `
            <tr>
                <td>${libro.nombreDelLibro}</td>
                <td>${libro.nombreDelAutor}</td>
                <td>${libro.categoria}</td>
                <td>
                    <button onclick="abrirPDF('${pdfUrl}')">Ver PDF</button>
                </td>
            </tr>`;
        librosTableBody.innerHTML += row; // Añadir cada fila a la tabla
    });
}
// Función para filtrar los libros según el término de búsqueda
function filtrarLibros() {
    const termino = document.getElementById("searchInput").value.toLowerCase();
    const librosFiltrados = todosLosLibros.filter(libro =>
        libro.nombreDelLibro.toLowerCase().includes(termino) ||
                libro.nombreDelAutor.toLowerCase().includes(termino)
    );
    mostrarLibros(librosFiltrados); // Actualiza la tabla con los resultados filtrados
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
                        pdfUrl = `http://192.168.131.147:5500/${libro.archivoPdf}`;
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
                            ${pdfUrl ? `<button onclick="abrirPDF('${pdfUrl}')">Ver PDF</button>` : 'No disponible'}
                        </td>
                    </tr>`;
                    librosTableBody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error al cargar libros:', error));
}



// Función para cargar libros
function filtrarLibros() {
    const termino = document.getElementById("searchInput").value.toLowerCase();
    const filas = document.querySelectorAll("#librosTableBody tr");

    filas.forEach(fila => {
        const celdas = fila.getElementsByTagName("td");
        let coincide = false;

        // Iterar por cada celda y verificar si contiene el término de búsqueda
        for (let i = 0; i < celdas.length - 1; i++) { // -1 para ignorar la celda de acciones
            if (celdas[i].textContent.toLowerCase().includes(termino)) {
                coincide = true;
                break;
            }
        }

        // Mostrar u ocultar la fila dependiendo de si coincide
        fila.style.display = coincide ? "" : "none";
    });
}


// Función para abrir el PDF en un modal
function abrirPDF(pdfUrl) {
    const embed = document.getElementById('pdfEmbed');
    embed.src = pdfUrl; // Establecer la fuente del PDF
    const modal = new bootstrap.Modal(document.getElementById('pdfModal'));
    modal.show(); // Mostrar el modal
}


function sanitizar(texto) {
    if (!texto)
        return ''; // Retornar vacío si el texto es nulo o indefinido
    return texto
            .replace(/&/g, '&amp;') // Reemplazar & por &amp;
            .replace(/</g, '&lt;')  // Reemplazar < por &lt;
            .replace(/>/g, '&gt;')  // Reemplazar > por &gt;
            .replace(/"/g, '&quot;') // Reemplazar " por &quot;
            .replace(/'/g, '&#39;'); // Reemplazar ' por &#39;
}