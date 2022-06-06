const api = '/api/';
let basicForm = $('#basic-ad-form');
let carForm = $('#car-ad-form');
let typeForm;

async function postAd(path, body) {
    let headers = {
        'content-type': 'application/json', Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
    };

    let options = {
        method: 'POST', headers: new Headers(headers),
    };

    options.body = JSON.stringify(body);
    await fetch(api + path, options)
        .then((response) => response.json())
        .then((json) => {
            console.log(json);
            window.location.href = "/components/ads/list-ads.html?ads=my_ads";
        })
        .catch((error) => console.log(error));
}

document.body.addEventListener('submit', async function (event) {
    let body;
    let path;
    event.preventDefault();
    switch (typeForm) {
        case 'basic':
            path = '/basic-ads';
            body = saveBasicAd().then((body) => postAd(path, body));
            break;
        case 'car':
            path = '/car-ads';
            body = saveCarAd().then((body) => postAd(path, body));

            break;
    }
});

function loadNavBar() {
    $('#navbar').load('../../html/navbar.html');
}

function checkBasicAd() {
    typeForm = 'basic';
    carForm.hide();
    basicForm.show();
}

function checkCarAd() {
    typeForm = 'car';
    basicForm.hide();
    carForm.show();
}

async function getBinaryBlob(file) {
    return new Promise((resolve) => {
        let reader = new FileReader();
        reader.onloadend = function () {
            resolve(reader.result.split(',')[1]);
        };
        reader.readAsDataURL(file);
    });
}

async function getImgArrBinary(arr) {
    let promises = [];
    for (let index = 0; index < arr.files.length; index++) {
        promises.push(getBinaryBlob(arr.files[index]));
    }
    return await Promise.all(promises).then((values) => {
        return values;
    });
}

async function saveBasicAd() {
    let title = $('#title').val();
    let description = $('#description').val();
    let img_arr = document.getElementById($('#basic_img_arr').attr('id'));
    let imageList = await getImgArrBinary(img_arr);

    return {
        title, description, imageList,
    };
}

async function saveCarAd() {
    let maker = $('#maker').val();
    let model = $('#model').val();
    let year = $('#year').val();
    let img_arr = document.getElementById($('#car_img_arr').attr('id'));
    let imageList = await getImgArrBinary(img_arr);

    return {
        maker: maker, model: model, year: year, imageList: imageList,
    };
}

window.onload = () => {
    loadNavBar();
    basicForm.hide();
    carForm.hide();
};
