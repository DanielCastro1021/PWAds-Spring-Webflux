let CACHE_NAME = 'cache-v1';
let staticAssets = [
    '/manifest.json',
    '/index.html',
    '/css/style.css',
    '/html/fallout.html',
    '/html/navbar.html',
    '/js/app.js',
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


self.addEventListener('message', (event) => {
    const sseURL = "http://localhost:8080/api/messages/received/sse";
    let userSSE = sseURL + "/" + event.data;
    const evtSource = new EventSource(userSSE);
    let callback = (str) => {
       postMessage(str);
    }
    evtSource.onmessage = function (e) {
        alert(e.data);
        callback(e.data)
    }
});


