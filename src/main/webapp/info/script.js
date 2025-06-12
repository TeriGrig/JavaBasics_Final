const colors = [
    '--color-List-1',
    '--color-List-2',
    '--color-List-3',
    '--color-List-4',
    '--color-List-5',
    '--color-List-6',
    '--color-List-7',
    '--color-List-8',
];

let count = 0;
const color = document.getElementById('color');
const appointments = document.getElementById('appointments');
const inside = document.getElementById('inside');
const userImage = document.getElementById('userImage');
const add = document.getElementById('add');
const days = document.querySelectorAll('.day');

const details = document.querySelectorAll('.details');

details.forEach((el, index) => {
    el.style.animationDelay = `${index * 0.2}s`;
});


days.forEach(day => {
    day.addEventListener('click', () => {
        if (day.style.backgroundColor === 'green') {
            day.style.backgroundColor = 'transparent';
            day.style.color = 'black';
        } else {
            day.style.backgroundColor = 'green';
            day.style.color = 'white';
        }
    });
});

function changeColor() {
    count = (count + 1) % colors.length;
    color.style.setProperty('background-color', `var(${colors[count]})`);
    appointments.style.setProperty('background', `var(${colors[count] + '-b'})`);
}

function addFunc(){
    add.style.setProperty('top', '-200%');
    inside.style.setProperty('top', '150%');
    userImage.style.setProperty('top', '150%');
    add.style.setProperty('background', `var(${colors[count] + '-b'})`);
}
function closeAdd(){
    add.style.setProperty('top', '-50%');
    inside.style.setProperty('top', '0');
    userImage.style.setProperty('top', '15%');
}

function openApp(){
    appointments.style.setProperty('top', 'var(--appointment-top)');
    inside.style.setProperty('top', 'var(--inside-top)');
    userImage.style.setProperty('top', '-100%');
}
function closeApp(){
    appointments.style.setProperty('top', '-250%');
    inside.style.setProperty('top', '0');
    userImage.style.setProperty('top', '15%');
}

function showOptions() {
    const selection = document.getElementById("property").value;
    const patientOptions = document.getElementById("patient-options");
    const doctorOptions = document.getElementById("doctor-options");

    // Hide both initially
    patientOptions.classList.add("hidden");
    doctorOptions.classList.add("hidden");

    // Show based on selection
    if (selection === "Doctor") {
        doctorOptions.classList.remove("hidden");
    } else if (selection === "Patient") {
        patientOptions.classList.remove("hidden");
    }
}

// Optional: hide both fields on page load
window.onload = function() {
    showOptions();
};