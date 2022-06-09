const ads_api = '/api/ads';
const ws_url = "ws://localhost:8080/ws/ads";
let ads = [];
let ads_type;
let ws = null;

function loadNavBar() {
    $('#navbar').load('/html/navbar.html', () => {
        $('#dropdownMenuButton2').toggleClass('active');
    });
}

function checkUserLogged() {
    if (localStorage.length > 0 && localStorage.getItem('token') !== null) $('#my-ads-btn').show(); else $('#my-ads-btn').hide();
}

function transitionAdsList() {
    let $ad_list = $('#ads-list');
    $('#sidebar>a.active').removeClass('active');
    $ad_list.empty();
    $ad_list.fadeIn('slow');
}

function getHtmlAdItem(ad) {
    let $htmlAdItem = $('<li>')
        .addClass('list-group-item list-group-item-action flex-column align-items-start')
        .css('display', 'grid');
    let $title = $('<h4>');
    let $description = $('<p>');
    let $owner = $('<small>');
    let $date = $('<small>');
    let dateParsed = new Date(ad['createdDate']).toUTCString();
    //Basic Ad
    if (ad.hasOwnProperty('title')) {
        $title.text(ad['title']);
        $description.text(ad['description']);
    }
    //Car Ad
    else if (ad.hasOwnProperty('maker')) {
        $title.text(ad['maker'] + ' ' + ad['model']);
        $description.text('Car for sale: ' + ad['maker'] + ' ' + ad['model'] + ' of year ' + ad['year']);
    }

    $owner.text(`Submitted by: ${ad['owner']}`);
    $date.text(`Submitted at: ${dateParsed}`);
    $htmlAdItem.append([$title, $description, $owner, $date]);
    $htmlAdItem.click(() => {
        window.location.href = `view-ad.html?id=${ad['id']}`;
    });
    $('#ads-list').append($htmlAdItem);
}

function loadAdList() {
    ads.forEach(getHtmlAdItem);
}


async function fetchMyAds() {
    let headers = {
        'Content-type': 'application/json', Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
    };
    const options = {
        method: 'GET', headers: new Headers(headers),
    };
    return await fetch(ads_api + '/personal', options)
        .then((response) => {
            return response.json();
        })
        .catch((err) => {
            return [];
        });
}

async function fetchAllCarAds() {
    let headers = {};
    const options = {
        method: 'GET', headers: headers,
    };
    return await fetch("/api/car-ads", options)
        .then((response) => {
            return response.json();
        })
        .catch((err) => {
            return [];
        });
}

async function fetchAllBasicAds() {
    let headers = {};
    const options = {
        method: 'GET', headers: headers,
    };
    return await fetch("/api/basic-ads", options)
        .then((response) => {
            return response.json();
        })
        .catch((err) => {
            return [];
        });
}

async function fetchAllAds() {
    let headers = {};
    const options = {
        method: 'GET', headers: headers,
    };
    return await fetch(ads_api, options)
        .then((response) => {
            return response.json();
        })
        .catch((err) => {
            return [];
        });
}


function getMyAds() {
    fetchMyAds()
        .then((json) => {
            console.log(json);
            if (json.length > 0) ads = json; else {
                $('#ads-list').append('<p>No ads were found.</p>');
                reject("Empty ads lists!");
            }
        })
        .then(loadAdList)
        .then(webSocketAdListener)
        .catch(err => console.log(err));
}

function getCarAds() {
    fetchAllCarAds()
        .then((json) => {
            console.log(json)
            if (json.length > 0) ads = json; else {
                $('#ads-list').append('<p>No ads were found.</p>');
                reject("Empty ads lists!");
            }
        })
        .then(loadAdList)
        .then(webSocketAdListener)
        .catch(err => console.log(err));
}

function getBasicAds() {
    fetchAllBasicAds()
        .then((json) => {
            console.log(json)
            if (json.length > 0) ads = json; else {
                $('#ads-list').append('<p>No ads were found.</p>');
                reject("Empty ads lists!");
            }
        })
        .then(loadAdList)
        .then(webSocketAdListener)
        .catch(err => console.log(err));
}

function getAllAds() {
    fetchAllAds()
        .then((json) => {
            console.log(json)
            if (json.length > 0) ads = json; else {
                $('#ads-list').append('<p>No ads were found.</p>');
                reject("Empty ads lists!");
            }
        })
        .then(loadAdList)
        .then(webSocketAdListener)
        .catch(err => console.log(err));
}

function loadAds() {
    transitionAdsList();
    let title = $('#list-ad-title');
    switch (ads_type) {
        case 'all':
            $('#all-ads-btn').addClass('active');
            title.text('All Ads List');
            getAllAds();
            break;
        case 'basic':
            $('#basic-ads-btn').addClass('active');
            title.text('All Basic Ads List');
            getBasicAds();
            break;
        case 'car':
            $('#car-ads-btn').addClass('active');
            title.text('All Car Ads List');
            getCarAds();
            break;
        case 'my_ads':
            $('#my-ads-btn').addClass('active');
            title.text('My Ads List');
            getMyAds();
            break;
    }
}

function webSocketAdListener() {
    ws = new WebSocket(ws_url);
    ws.onopen = () => {
        console.log("Active Ad WebSocket.")
    };
    ws.onmessage = (event) => {
        let ad = JSON.parse(event.data);
        console.log("WebSocket => New Ad:" + event.data);
        console.log("WebSocket => New Ad:" + ad);
        getHtmlAdItem(ad);
    };

    ws.onclose = (event) => {
        console.log("Close Ad WebSocket.")
    }

}

window.onload = () => {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    ads_type = urlParams.get('ads');
    checkUserLogged();
    loadNavBar();
    loadAds();
};

window.onunload = () => {
    if (ws) ws.close();
};
