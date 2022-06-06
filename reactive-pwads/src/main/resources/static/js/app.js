if ('serviceWorker' in navigator) {
    //Try to install SW if not ready
    window.addEventListener('load', function () {
        let register;
        navigator.serviceWorker
            .register('/sw.js', {scope: '/'})
            .then((reg) => {
                if (reg.installing) {
                    console.log('Service worker installing');
                } else if (reg.waiting) {
                    console.log('Service worker installed');
                } else if (reg.active) {
                    console.log('Service worker active');
                }
            })
            .catch(function (error) {
                // registration failed
                console.log('Registration failed with ' + error);
            });
    });
}

function loadNavBar() {
    $('#navbar').load('../html/navbar.html', () => {
        $('#home-tab-button').toggleClass('active');
    });
}

window.onload = function () {
    // load navbar
    loadNavBar();
};
