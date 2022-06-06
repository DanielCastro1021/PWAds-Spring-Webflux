const ads_api = '/api/ads';

const msg_api = '/api/messages';
let current_ad;
let ad_edit_div = $('#ad-edit');
let ad_view_div = $('#ad-view');
let basicAdForm = $('#basic-ad-form');
let carAdForm = $('#car-ad-form');


async function fetchSaveMessage(msg) {
    let headers = {
        'Content-type': 'application/json', Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
    };
    const options = {
        method: 'POST', headers: new Headers(headers),
    };
    options.body = JSON.stringify(msg);
    return await fetch(msg_api + '/', options)
        .then((response) => {
            if (response.status !== 200) {
                throw new Error('Not 200 response');
            } else return response.json();
        })
        .catch((error) => {
            console.log(error);
        });
}

function sendMessage() {
    let $msg = $('#message-text').val();
    let adId = current_ad['id'];
    let adOwnerUsername = current_ad['owner'];
    let message_body = {to: adOwnerUsername, adId, message: $msg};
    fetchSaveMessage(message_body).then((r) => {
        console.log(r);
        $('#message-text').val('');
    });
}

function loadImgList(img_arr) {
    for (let index in img_arr) {
        let $div = $('<div>').addClass('item');
        let $img = $('<img/>', {
            id: `img-${index}`, src: `data:image/png;base64,${img_arr[index]}`, alt: 'MyAlt',
        });
        $img.attr('style', 'max-width: 100%; height: auto; object-fit: contain;margin-left: auto;margin-right: auto;');
        $div.append($img);
        if (index === '0') $div.addClass('active');
        if (img_arr.length > 1) $('#carousel-controls').hide(); else $('#carousel-controls').show();
        $('.carousel-inner').append($div);
    }
}

function loadNoImageFound() {
    let $div = $('<div>').addClass('item');
    let $img = $('<img alt="no-image" src="../../img/broken-1.png"/>');
    $img.attr('style', 'max-width: 100%; height: auto; object-fit: contain;margin-left: auto;margin-right: auto;');
    $div.append($img);
    $div.addClass('active');
    $('#carousel-controls').hide();
    $('.carousel-inner').append($div);
}

function loadAd(ad) {
    let $htmlAd = $('<li>')
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
    $htmlAd.append([$title, $description, $owner, $date]);
    $htmlAd.click(() => {
        window.location.href = `view-ad.html?id=${ad['id']}`;
    });
    if (!ad.hasOwnProperty('imageList') || ad['imageList'] === null || ad['imageList'].length <= 0) loadNoImageFound(); else loadImgList(ad['imageList']);
    $('#ad-data').append($htmlAd);
    return ad;
}

function loadNavBar() {
    $('#navbar').load('../../html/navbar.html', () => {
        checkUserLogged();
        $('#navbar>a.active').removeClass('active');
    });
}

function checkUserLogged() {
    if (localStorage.length > 0 && localStorage.getItem('token') != null) $('#my-ads-btn').show(); else $('#my-ads-btn').hide();
}

function checkOwner() {
    const actions = $('#owner-actions');
    const messagePanel = $('#ad-message-panel');
    const token = localStorage.getItem('token');
    if (token) {
        const decode = JSON.parse(atob(token.split('.')[1]));

        if (decode.sub === current_ad['owner']) {
            actions.show();
            messagePanel.hide();
        } else {
            actions.hide();
            messagePanel.show();
        }
    } else {
        actions.hide();
        messagePanel.hide();
    }
}

async function fetchDeleteAd() {
    let headers = {
        'Content-type': 'application/json', Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
    };
    const options = {
        method: 'DELETE', headers: new Headers(headers),
    };
    let deleteURL = "/api/ads/" + current_ad['id'];
    return await fetch(deleteURL, options)
        .then((response) => {
            if (response.ok) {
                return response;
            }
            throw new Error('Something went wrong');
        })
        .then((response) => {
            return response.json();
        })
        .catch((err) => (console.log(err)));
}

async function fetchUpdateAd() {
    let headers = {
        'Content-type': 'application/json', Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
    };
    const options = {
        method: 'PUT', headers: new Headers(headers),
    };
    options.body = JSON.stringify(current_ad);
    let updateURL;
    if (current_ad.hasOwnProperty("title")) updateURL = "/api/basic-ads/"; else if (current_ad.hasOwnProperty("maker")) updateURL = "/api/car-ads/";

    return await fetch(updateURL + current_ad['id'], options)
        .then((response) => {
            if (response.status !== 200) {
                throw new Error('Not 200 response');
            } else return response.json();
        })
        .catch((error) => {
            console.log(error);
        });
}

async function fetchAd(id) {
    let headers = {
        'Content-type': 'application/json', Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
    };
    const options = {
        method: 'GET', headers: new Headers(headers),
    };
    return await fetch(ads_api + '/' + id, options)
        .then((response) => {
            if (response.status !== 200) {
                throw new Error('Not 200 response');
            } else return response.json();
        })
        .catch((error) => {
            window.location.replace('http://localhost:8080/fallout.html');
            console.log(error);
        });
}

async function getImgArrBinary(arr) {
    let promises = [];
    for (let index = 0; index < arr.files.length; index++) {
        promises.push(getBinaryBlob(arr.files[index]));
    }
    return await Promise.all(promises).then((values) => {
        return values || [];
    });
}

async function getBasicAdValues() {
    let title = $('#title').val();
    let description = $('#description').val();
    let img_arr = document.getElementById($('#basic_img_arr').attr('id'));
    let imageList = await getImgArrBinary(img_arr);
    current_ad['title'] = title;
    current_ad['description'] = description;
    if (imageList.length > 0) current_ad['imageList'] = imageList;
}

async function getCarAdValues() {
    let maker = $('#maker').val();
    let model = $('#model').val();
    let year = $('#year').val();
    let img_arr = document.getElementById($('#car_img_arr').attr('id'));
    let imageList = await getImgArrBinary(img_arr);

    current_ad['model'] = model;
    current_ad['maker'] = maker;
    current_ad['year'] = year;
    if (imageList.length > 0) current_ad['imageList'] = imageList;
}

function updateBasicAd() {
    getBasicAdValues()
        .then(fetchUpdateAd)
        .then((r) => {
            window.location.reload();
        });
}

function updatedCarAd() {
    getCarAdValues()
        .then(fetchUpdateAd)
        .then((r) => {
            window.location.reload();
        });
}

function toggleEdit() {
    if (current_ad.hasOwnProperty('title')) {
        $('#title').val(current_ad['title']);
        $('#description').val(current_ad['description']);
        basicAdForm.show();
        carAdForm.hide();
    } else if (current_ad.hasOwnProperty('maker')) {
        $('#maker').val(current_ad['maker']);
        $('#model').val(current_ad['model']);
        $('#year').val(current_ad['year']);
        carAdForm.show();
        basicAdForm.hide();
    }
    ad_edit_div.show();
    ad_view_div.hide();
}

function cancelUpdate() {
    ad_edit_div.hide();
    ad_view_div.show();
}

function deleteAd() {
    fetchDeleteAd()
        .then((res) => {
            history.back();
        });
}

window.onload = () => {
    loadNavBar();
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);
    const id = urlParams.get('id');
    ad_edit_div.hide();
    fetchAd(id)
        .then((ad) => {
            current_ad = ad;
            loadAd(current_ad);
        })
        .then(checkOwner);

    $('.carousel').carousel({
        interval: 2000,
    });
};
