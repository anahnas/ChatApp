var socket;
var host = "ws://localhost:8080/WAR2020/ws";
let username = '';
let sessionId = '';

window.onload = function() {
	
    username = sessionStorage.getItem('username');
    if (username !== "" && username !== undefined) {
	    var pLoggedInAs = document.getElementById('loggedInAs');
		pLoggedInAs.innerHTML = 'Logged in as <b>' + username + '</b>.';
		
		$.ajax({
		url: "rest/chat/registered",
		type: "GET",
		success: function(data) {
			var selectUsers = document.getElementById('select-users');
			var opt = document.createElement('option');
			opt.value = "All";
			opt.innerHTML = "All";
			selectUsers.appendChild(opt);

			for (usrname of data) {
				var opt = document.createElement('option');
				opt.value = usrname;
			    opt.innerHTML = usrname;
			    selectUsers.appendChild(opt);
			}

			$('.selectpicker').selectpicker('refresh');
        }
		});
	} else {
		window.location.href='./index.html';
	}
}


$(document).ready(function() {
	// let user = null;
    username = sessionStorage.getItem('username');
    
	$('.selectpicker').selectpicker();

    
    $("#logout-link").click(function() {
		if (username !== "" && username !== undefined) {
			$.ajax({
				url: "rest/chat/loggedIn/" + username,
				type: "DELETE",
				data: "",
				complete: function(data) {
					sessionStorage.setItem('username', "");
	                alert("User logged out");
	                window.location.href='./login.html';
	            }
			});
		} else {
			window.location.href='./index.html';
		}
	});
    
    var div = document.getElementById('showMessage');
	div.innerHTML = "";
	var selectUsers = document.getElementById('select-users');
	selectUsers.innerHTML = "";
	document.getElementById('subject').value = "";
	document.getElementById('message-content').value = "";

	$.ajax({
		url: "rest/chat/registered",
		type: "GET",
		success: function(data) {

			$('.selectpicker').selectpicker('refresh');
        }
	});
	
	
	$("#btnSendMessage").click(function() {
		var div = document.getElementById('showMessage');
		div.innerHTML = "";

		var receiverUsername = $("#select-users").val();
		if (receiverUsername === "" || receiverUsername === undefined) {
			var div = document.getElementById('showMessage');
			alert('Select user!');
			return;
		}

		var messageTitle = $("#subject").val();
		if (messageTitle === "" || messageTitle === undefined) {
			var div = document.getElementById('showMessage');
			alert('Title should not be empty');
			return;
		}

		var messageContent = $("#message-content").val();
		if (messageContent === "" || messageContent === undefined) {
			var div = document.getElementById('showMessage');
			alert('Content should not be empty');
			return;
		}

		if (receiverUsername === "All") {
			
			$.ajax({
			url: "rest/chat/messages/all",
			type: "POST",
			data: JSON.stringify({"senderUsername":username, "receiverUsername": "all", "messageContent":messageContent, "messageTitle": messageTitle}),
			contentType: "application/json",
			success: function(data) {
                var div = document.getElementById('showMessage');
                alert('Message has been sent to all users');
				return;
            },
            error: function(err) {
                var div = document.getElementById('showMessage');
                alert('Message could not be sent to all users');
				return;
            }
			}); 
		} else {
			$.ajax({
				url: "rest/chat/messages/user",
				type: "POST",
				data: JSON.stringify({"senderUsername":username, "receiverUsername": receiverUsername, "messageContent":messageContent, "messageTitle": messageTitle}),
				contentType: "application/json",
				success: function(data) {
	            	var div = document.getElementById('showMessage');
	            	alert('Message has been sent to user');
					return;
	            },
	            error: function(err) {
	                var div = document.getElementById('showMessage');
	                alert('Message could not be sent to user');
					return;
	            }
			});
		}
		$('#select-users').val('');
		$('#subject').val('');
		$('#message-content').val('');
	});
		
	

	
	
	$("#btnPost").click(function() {
		$.ajax({
			url: "rest/chat/post/ananana",
			type: "POST",
			data: {},
			contentType: "application/json",
			dataType: "json",
			complete: function(data) {
				console.log('Sent message to the server.');
				}
			});
		});
	
	try {
	    // socket = new WebSocket(host);
		socket = new WebSocket(host + "/" + sessionStorage.getItem('username'));
		console.log('connect: Socket status: '+socket.readyState);

		socket.onopen = function() {
			console.log('onopen: Socket status: '+socket.readyState+' (open)');
		}
		socket.onmessage = function(msg) {
			console.log('onmessage: Received: '+msg.data);
		}

		socket.onclose = function() {
			socket = null;
		}
	} catch(exception) {
		console.log('Error'+exception);
	}

	$('#loggedIn').click(function() {
		$.ajax({
			url: "rest/chat/loggedIn",
			type: "GET",
			contentType: "application/json",
			success: function(data) {
				let allUsers = "";
				// var loggedInUser = data.responseJSON;
				for (usr of data) {
					allUsers += "Username: " + usr + "\n";
				}
				alert(allUsers);
			}
		});
		
	});
	
	$('#registered').click(function() {
		$.ajax({
			url: "rest/chat/registered",
			type: "GET",
			success: function(data) {
				let allUsers = "";
				for (usr of data) {
					allUsers += "Username: " + usr + "\n";
				}
				alert(allUsers);
			}
		});
	

});
	
	
});
