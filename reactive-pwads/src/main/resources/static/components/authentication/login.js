const login_api_url = '/api/login';


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
    let user = {username, password};
    postLogin(user)
        .then((json) => {
            console.log(json);
            localStorage.setItem('token', json['token']);
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
