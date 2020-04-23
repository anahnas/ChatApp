var socket;
var host = "ws://localhost:8080/WAR2020/ws";

$(document).ready(function() {
	
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
		socket = new WebSocket(host);
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
			success: function(data) {
				let allUsers = "";
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
	
/*$('#logout').click(function() {
		
		$.ajax({
			url: "rest/chat/logout/" + username,
			type: "DELETE",
			data: JSON.stringify({"username":username, "password":password}),
			contentType: "application/json",
			complete: function(data) {
				
				console.log("Logout successfull. \nUsername: " + username);
				window.location.href='./login.html';
			}
		});
	});*/
	
});

// random uuid
function uuidv4() {
	  return ([1e7]+-1e3+-4e3+-8e3+-1e11).replace(/[018]/g, c =>
	    (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
	  )
	}

console.log(uuidv4());