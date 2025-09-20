// Ejemplo: alerta al enviar formularios
document.addEventListener('DOMContentLoaded', function() {
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function() {
            alert('Datos enviados correctamente!');
        });
    });
});
