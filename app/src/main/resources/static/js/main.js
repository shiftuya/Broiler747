let citiesFromSelect = document.getElementById('cities-from');
let citiesToSelect = document.getElementById('cities-to');
let departureDateInput = document.getElementById('departure-date');
let maxConnectionsInput = document.getElementById('max-connections');

let pathsNumberH1 = document.getElementById('paths-number');
let pathsOl = document.getElementById('paths');

let passengerIdInput = document.getElementById('passenger-id');
let passengerNameInput = document.getElementById('passenger-name');
let passengerPhoneInput = document.getElementById('passenger-phone');
let passengerEmailInput = document.getElementById('passenger-email');
let fareConditionsSelect = document.getElementById('fare-conditions');

fetch('/cities', {method: 'GET'})
    .then(response => response.json())
    .then(cities => cities.forEach(city => {
        let value = JSON.parse(city.name).en;
        citiesFromSelect.appendChild(createOption(value));
        citiesToSelect.appendChild(createOption(value));
    }));

document.getElementById('find-paths').onclick = function() {
    fetch('/paths?departurePoint=' + citiesFromSelect.value +
                '&arrivalPoint=' + citiesToSelect.value +
                '&departureDate=' + departureDateInput.value +
                '&connections=' + maxConnectionsInput.value,
            {method: 'GET'})
        .then(response => response.json())
        .then(paths => {
            pathsNumberH1.innerHTML = 'Found ' + paths.length + ' paths'
            paths.forEach(path => {
                let li = document.createElement('li');
                for (let i = 0; i < path.points.length; i++) {
                    if (i > 0) li.innerHTML += ' -> ';
                    li.innerHTML += path.points[i].city + ' (' + path.points[i].code + ')';
                }
                li.innerHTML += ' [' + (path.points.length - 2) + ' connections]';
                let buyButton = document.createElement('button');
                buyButton.innerHTML = 'Buy ticket';
                buyButton.onclick = function() {
                    delete path.points;
                    let booking = {
                        path: path,
                        passengerId: passengerIdInput.value,
                        passengerName: passengerNameInput.value,
                        contact: {
                            phone: passengerPhoneInput.value,
                            email: passengerEmailInput.value
                        },
                        fareConditions: fareConditionsSelect.value
                    };
                    fetch('/booking', {method: 'POST', body: JSON.stringify(booking)})
                        .then(response => response.json())
                        .then(booking => alert(booking.bookingId));
                }
                li.appendChild(buyButton);
                pathsOl.appendChild(li);
            });
        });

    return false;
};

function createOption(value) {
    let opt = document.createElement('option');
    opt.value = value;
    opt.innerHTML = value;
    return opt;
}
