let CACHE_NAME = 'cache-v1';

let staticAssets = [
    '/manifest.json',
    '/index.html',
    '/css/style.css',
    '/html/fallout.html',
    '/html/navbar.html',
    '/js/app.js',
    '/js/firebase-notifications.js',
    '/icons/ios/100.png',
    '/icons/ios/144.png',
    '/img/home-img-placeholder.png',
    '/img/home-img-placeholder-340_x_220.png',
    '/img/home-img-placeholder-480_x_300.png',
    '/img/broken-1.png',
    '/components/ads/list-ads.html',
    '/components/ads/list-ads.js',
    '/components/ads/list-ads.html?ads=all',
    '/components/ads/list-ads.html?ads=basic',
    '/components/ads/list-ads.html?ads=car',
    '/components/authentication/login.html',
    '/components/authentication/login.js',
    '/components/authentication/register.html',
    '/components/authentication/register.js',
    "https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js",
    'https://code.jquery.com/jquery-3.6.0.min.js',
    'https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.2/dist/umd/popper.min.js',
    'https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css',
    'https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js',
    'https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.min.js',
    'https://www.gstatic.com/firebasejs/6.6.2/firebase-app.js',
    'https://www.gstatic.com/firebasejs/6.6.2/firebase-messaging.js',
];

self.addEventListener('install', (event) => {
    // Precache assets on install
    event.waitUntil(
        caches.open(CACHE_NAME).then((cache) => {
            return cache.addAll(staticAssets);
        })
    );
});

self.addEventListener('activate', (event) => {
    event.waitUntil(
        (async () => {
            const cacheNames = await caches.keys();
            await Promise.all(
                cacheNames.map(async (cacheName) => {
                    if (CACHE_NAME !== cacheName) {
                        await caches.delete(cacheName);
                    }
                })
            );
        })()
    );
});

self.addEventListener('fetch', (event) => {
    event.respondWith(caches.open(CACHE_NAME).then((cache) => {
        // Go to the network first
        return fetch(event.request).then((fetchedResponse) => {
            if (event.request.method === "GET") {
                cache.put(event.request, fetchedResponse.clone());
            }
            // Return the network response
            return fetchedResponse;
        }).catch(() => {
            if (event.request.method !== 'GET') {
                return Promise.reject('no-match');
            }
            // If the network is unavailable, get
            return cache.match(event.request.url).then((cachedResponse) => {
                // Return a cached response if we have one
                if (cachedResponse) {
                    return cachedResponse;
                } else {
                    return cache.match('/html/fallout.html');
                }
            });
        });
    }));
});


self.addEventListener('push', (event) => {
    let payload = event.data ? event.data.text() : 'no payload';
    payload = JSON.parse(payload);
    let username = payload.notification.title;
    let message = payload.notification.body;

    // Customize notification here
    const notificationTitle = 'PWAds - New message received';
    const notificationOptions = {
        body: `The user ${username} sent you the following message: ${message}`,
        icon: '/icons/ios/100.png',
    };

    self.registration.showNotification(notificationTitle, notificationOptions);
});

// service-worker.js
self.addEventListener('message', (event) => {
    console.log(`The client sent me a message: ${event.data}`);
    getToken(event);
});

//Firebase Notification Config

importScripts('https://www.gstatic.com/firebasejs/6.6.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/6.6.2/firebase-messaging.js');

const vapidKey =
    'BCX_it2GStqQGsv1IJRzyLzCjEvo-adRhY47KUKvOBGhd0nNH2xyRN90oojy0Da7s66vZ-FwbECkdj9OAXNfMWw';

const firebaseConfig = {
    apiKey: "AIzaSyBH0o09K98VCIAfX2ng8nKj0-_2pbAIPUk",
    authDomain: "pwads-app.firebaseapp.com",
    projectId: "pwads-app",
    storageBucket: "pwads-app.appspot.com",
    messagingSenderId: "63582499105",
    appId: "1:63582499105:web:0ac476f856ae32833a4e60",
    measurementId: "G-2YKNJ02RNL"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

// Initialize Firebase Cloud Messaging and get a reference to the service
const messaging = firebase.messaging();


function getToken(event) {
    messaging
        .getToken({
            vapidKey: vapidKey,
            serviceWorkerRegistration: self.registration,
        })
        .then((currentToken) => {
            if (currentToken) {
                console.log('current token for client: ', currentToken);
                event.source.postMessage(currentToken);
                // Track the token -> client mapping, by sending to backend server
                // show on the UI that permission is secured
            } else {
                console.log(
                    'No registration token available. Request permission to generate one.'
                );
                // shows on the UI that permission is required
            }
        })
        .catch((err) => {
            console.log('An error occurred while retrieving token. ', err);
            // catch error while creating client token
        });
}
