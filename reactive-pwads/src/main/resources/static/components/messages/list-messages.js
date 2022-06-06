const msg_api = '/api/messages';
let msg_type;
let msg_list=  $('#msg-list');
function loadNavBar() {
  $('#navbar').load('../../html/navbar.html', () => {
    $('#messages-tab-button').toggleClass('active');
  });
}

function transitionMessageList() {
  $('#sidebar>a.active').removeClass('active');
 msg_list.empty();
  if (msg_type === 'sent') {
    $('#sent-msg-btn').addClass('active');
    $('#list-msg-title').text('Sent Messages').fadeIn('slow');
  } else if (msg_type === 'received') {
    $('#received-msg-btn').addClass('active');
    $('#list-msg-title').text('Received Messages').fadeIn('slow');
  }
  msg_list.fadeIn('slow');
}

function getAdHtmlItem(ad) {
  let $a = $('<a>');
  if (ad !== null) {
    let title;
    if (ad.hasOwnProperty('title')) {
      title = ad['title'] || 'Error';
    } else if (ad.hasOwnProperty('maker')) {
      title = ad['maker'] + ' ' + ad['model'] + ' ' + ad['year'];
    }
    $a.text(`Referent ad: ${title}`);
    $a.click(() => {
      window.location.href = `../ads/view-ad.html?id=${ad['id']}`;
    });
  }
  let $small = $('<small>').append($a);
  return $a;
}

function getDateHtmlItem(date) {
  let dateParsed = new Date(date).toUTCString();
  let $small = $('<small>');
  $small.text(`Submitted at: ${dateParsed}`);
  return $small;
}

function getMsgHtmlItem(msg) {
  let $p = $('<p>').addClass('mb-1');
  $p.text(`Message: ${msg}`);
  return $p;
}

function getUserHtmlItem(str, user) {
  let $p = $('<p>');
  $p.text(`${str}: ${user['username']}`);
  return $p;
}

function getHtmlMsgItem(msg) {
  let ad = getAdHtmlItem(msg['ad']);
  let date = getDateHtmlItem(msg['createdDate']);
  let message = getMsgHtmlItem(msg['message']);
  let user;
  if (msg_type === 'sent') {
    user = getUserHtmlItem('To', msg['to']);
  } else if (msg_type === 'received') {
    user = getUserHtmlItem('From', msg['from']);
  }
  let $li = $('<li>')
    .addClass(
      'list-group-item list-group-item-action flex-column align-items-start'
    )
    .css('display', 'grid');
  $li.append([user, date, ad, message]);
  $('#msg-list').append($li);
}

function loadMessageList(msgList) {
  transitionMessageList();
  if (msgList.length <= 0 && !msgList.hasOwnProperty('messageList')) {
    $('#msg-list').append('<p>No messages found.</p>');
  } else {
    msgList['messageList'].forEach(getHtmlMsgItem);
  }
}

async function fetchSentMessages() {
  let headers = {
    'Content-type': 'application/json',
    Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
  };
  const options = {
    method: 'GET',
    headers: headers,
  };
  return await fetch(msg_api + '/sent', options).then((response) => {
    return response.json();
  });
}

async function fetchReceivedMessages() {
  let headers = {
    'Content-type': 'application/json',
    Authorization: `Bearer ${localStorage.getItem('token')}`, // notice the Bearer before your token
  };
  const options = {
    method: 'GET',
    headers: headers,
  };
  return await fetch(msg_api + '/received', options).then((response) => {
    return response.json();
  });
}

function loadReceivedMessages() {
  $('#received-msg-btn').addClass('active');
  fetchReceivedMessages()
    .then((json) => {
      return json['_embedded'] || [];
    })
    .then(loadMessageList);
}

function loadSentMessages() {
  $('#sent-msg-btn').addClass('active');
  fetchSentMessages()
    .then((json) => {
      return json['_embedded'] || [];
    })
    .then(loadMessageList);
}

function loadMessages() {
  if (msg_type === 'sent') {
    loadSentMessages();
  } else if (msg_type === 'received') {
    loadReceivedMessages();
  }
}

window.onload = () => {
  const queryString = window.location.search;
  const urlParams = new URLSearchParams(queryString);
  msg_type = urlParams.get('messages');
  loadNavBar();
  loadMessages();
};
