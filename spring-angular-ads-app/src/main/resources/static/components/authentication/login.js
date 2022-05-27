const login_api_url = '/api/auth/login/';

if ('serviceWorker' in navigator) {

    //Try to install SW if not ready
    window.addEventListener('load', function () {
        navigator.serviceWorker
            .register('../../sw.js', {scope: '../../'})
            .then((reg) => {
                if (reg.installing) {
                    console.log('Service worker installing');
                } else if (reg.waiting) {
                    console.log('Service worker installed');
                } else if (reg.active) {
                    requestPermission();
                    console.log('Service worker active');
                }
            })
            .catch(function (error) {
                // registration failed
                console.log('Registration failed with ' + error);
            });
    });

}

async function postLogin(user) {
    const options = {
        method: 'POST',
        headers: new Headers({'content-type': 'application/json'}),
    };

    options.body = JSON.stringify(user);

    return await fetch(login_api_url, options)
        .then((response) => {
            if (response.status !== 200) {
                throw new Error('Not 200 response');
            } else return response.json();
        })
        .catch((error) => {
            throw new Error(error);
        });
}

function login() {
    let username = $('#username').val();
    let password = $('#password').val();
    let firebaseToken = localStorage.getItem('firebase-token');
    let user = {username, password, firebaseToken};
    postLogin(user)
        .then((json) => {
            console.log(json);
            localStorage.setItem('token', json['token']);
            localStorage.setItem('roles', json['roles']);
            localStorage.setItem('firebase-token', json['firebaseToken']);
            window.location.href = '../../index.html';
        })
        .catch((err) => {
            console.log(err);
        });
}

function loadNavBar() {
    $('#navbar').load('../../html/navbar.html');
}

window.onload = () => {
    loadNavBar();
};
