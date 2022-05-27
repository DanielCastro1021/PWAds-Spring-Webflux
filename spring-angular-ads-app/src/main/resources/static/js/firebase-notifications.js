function requestPermission() {
  // [START messaging_request_permission]
  Notification.requestPermission().then((permission) => {
    if (permission === 'granted') {
      console.log('Notification permission granted.');
      if ('serviceWorker' in navigator) {
        navigator.serviceWorker.addEventListener('message', (event) => {
          if (localStorage.getItem('firebase-token') === null)
            localStorage.setItem('firebase-token', event.data);
        });
        navigator.serviceWorker.ready
          .then(function (registration) {
            if (localStorage.getItem('firebase-token') === null) {
              registration.active.postMessage('token');
              console.log(
                'Registration successful, scope is:',
                registration.scope
              );
            }
          })
          .catch(function (err) {
            console.log('Service worker registration failed, error:', err);
          });
      }
    } else {
      console.log('Unable to get permission to notify.');
    }
  });
  // [END messaging_request_permission]
}
