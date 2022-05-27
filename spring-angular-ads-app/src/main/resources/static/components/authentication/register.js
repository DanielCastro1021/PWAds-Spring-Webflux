const register_api_url = '/api/auth/register/';


async function postRegister(user) {
  const options = {
    method: 'post',
    headers: new Headers({ 'content-type': 'application/json' }),
  };

  options.body = JSON.stringify(user);
  return await fetch(register_api_url, options)
    .then((response) => {
      if (response.status !== 200) {
        throw new Error('Not 200 response');
      } else return response.json();
    })
    .catch((error) => {
      console.log(error);
    });
}

function register() {
  let username = $('#username').val();
  let email = $('#email').val();
  let password = $('#password').val();
  let user = { username, email, password };
  postRegister(user).then((json) => {
    if (json.message === 'User registered successfully!') {
      localStorage.setItem('temp_username', username);
      window.location.replace(
        'http://localhost:8080/components/authentication/login.html'
      );
    }
  });
}

function loadNavBar() {
  $('#navbar').load('../../html/navbar.html');
}

window.onload = () => {
  if (localStorage.getItem('token') !== null)
    window.location.replace('http://localhost:8080/index.html');
  loadNavBar();
};
