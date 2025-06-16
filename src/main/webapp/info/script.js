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

let lastFocus= null;
let daysArray=[];
let selDay = null;

const category = document.body.dataset.category;

let count = 0;
let days = document.querySelectorAll('.day');
const color = document.getElementById('color');
const appointments = document.getElementById('appointments');
const inside = document.getElementById('inside');
const userImage = document.getElementById('userImage');
const add = document.getElementById('add');
const details = document.querySelectorAll('.details');
const cal = document.getElementById("calendar");
const today = new Date();
const dayNow = today.getDate();

// effect
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
details.forEach((el, index) => {
    el.style.animationDelay = `${index * 0.5}s`;
    el.addEventListener('click', saveEl);
});

// code
fetch('/JavaBasic/GetDays')
    .then(res => res.text())
    .then(data => {
        console.log(data);
        daysArray = JSON.parse(data);

        daysArray = daysArray.map(obj => obj.day);
        const daysSet = new Set(daysArray);
        days.forEach(day => {
            const inputValue = day.querySelector("input").value;
            if (daysSet.has(Number(inputValue))) {
                day.style.backgroundColor = 'green';
                day.style.color = 'white';
            }
        });
    })
    .catch(error => {
        console.error("Error fetching app:", error);
    });

fetch(window.location.origin + '/JavaBasic/getApp')
    .then(response => {
        if (!response.ok) {
            console.error("HTTP status:", response.status);
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
        const container = document.querySelector('.menuDetails');
        container.innerHTML = ''; // καθάρισε προηγούμενα

        data.forEach(item => {
            const div = document.createElement('div');
            div.className = 'details';
            div.setAttribute('tabindex', '-1');

            div.innerHTML = `
                <h2>${item.name}</h2>
                <h2>${item.day}</h2>
              `;
            if (item.day < dayNow){
                div.style.backgroundColor = "red"
            }else if (item.day == dayNow){
                div.style.backgroundColor = "green";
            }

        container.appendChild(div);
        });
    })
    .catch(error => {
        console.error("Τελικό σφάλμα:", error);
    });


days.forEach(day => {
    day.addEventListener('click', () =>{
        if (day.style.backgroundColor === 'transparent' || day.style.backgroundColor === ''){
            day.style.backgroundColor = 'green';
            day.style.color = 'white';
            selDay = day;
            let n = { "day": parseInt(day.querySelectorAll("input")[0].value) };
            daysArray.push(n);
            console.log(daysArray)
        }else if (day.style.backgroundColor === 'green') {
            day.style.backgroundColor = 'transparent';
            day.style.color = 'black';
            daysArray = daysArray.filter(d => d.day !== parseInt(day.querySelectorAll("input")[0].value));
        }
    })
});

if(category === "patients"){
    document.getElementById("doctor-type").addEventListener('change', () =>{
        daysArray = [];
        if (document.getElementById("doctor-type").value === ""){
            cal.style.display = "none";
        }else {
            fetch(`/JavaBasic/GetDays?findRole=${encodeURIComponent(document.getElementById("doctor-type").value)}`, {
                method: "GET"
            })
                .then(res => res.text())
                .then(data => {
                    console.log(data);
                    daysArray = JSON.parse(data);
                    const dayMap = new Map();
                    daysArray.forEach(obj => {
                        dayMap.set(obj.day, obj.available);
                    });
                    days.forEach(day => {
                        const d = Number(day.querySelector("input").value);
                        if (dayMap.has(d)) {
                            if (!dayMap.get(d)) {
                                day.style.backgroundColor = 'yellow';
                                day.style.color = 'black';
                            } else {
                                day.style.backgroundColor = 'transparent';
                                day.style.color = 'black';
                            }
                        } else {
                            day.style.backgroundColor = 'red';
                            day.style.color = 'black';
                        }
                    });
                })
                .catch(error => {
                    console.error("Error fetching appointments:", error);
                });
            cal.style.display = "block";
        }
    });
}

function saveEl(){
    lastFocus = this;
}
function Delete(){
    let id = lastFocus.querySelectorAll("input")[0];
    let role = lastFocus.querySelectorAll("input")[1];
    console.log(id.value + "\n" + role.value);
    fetch("DeleteUser", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "id=" + encodeURIComponent(id.value) + "&role=" + encodeURIComponent(role.value)
    })
        .then(response => response.text())
        .then(result => {
            alert("Delete: " + result);
            location.reload();
        })
        .catch(error =>{
            console.error("error ->" + error)
        })
}
function Save(){
    if (category === "admins"){
        document.getElementById('addUser').submit();
    }
    else if (category === "doctors"){
        let newArr = daysArray.map(obj => obj.day); // Αν το daysArray είναι [{day: 17}, {day: 25}, ...]
        newArr.sort((a, b) => a - b);
        console.log(newArr);

        fetch("SetDay", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(newArr)  // στέλνεις κανονικό JSON array, π.χ. [17, 19, 25]
        })
            .then(response => response.text())
            .then(data => console.log(data))
            .catch(error => console.log("Error:", error));

    }
    else{
        if (document.getElementById("doctor-type").value != null){
            fetch('SetDates', {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: "role=" + encodeURIComponent(document.getElementById("doctor-type").value) + "&day=" + encodeURIComponent(selDay.querySelectorAll("input")[0].value)
            })
                .then(response => response.text())
                .then(data => console.log(data),
                PatientDateClear()
        )
                .catch(error => console.log("Error:", error));
        }
        // document.getElementById("doctor-type").value;
        // selDay.querySelectorAll("input")[0].value;
    }
    alert("Ok", "Ok");
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
window.onload = function() {
    if (category === "admins") {
        showOptions();
    }
};