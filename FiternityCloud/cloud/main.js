
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("validatePush", function(request, response) {
  var senderUser = request.user;
  var senderName = request.params.name;
  var matchName = request.params.matchName;
  var recipientUserId = request.params.facebookId;
  var senderId = request.params.senderFacebookId;
  // Problem: Thinks it's GMT-0 when I'm trying to call it from GMT-7
  // Temporary solution: hardcoding GMT-7 values
  var startDate = new Date(request.params.startDate - 25200000);
  var endDate = new Date(request.params.endDate - 25200000);
  var exercise = request.params.exercise;
  var message = senderName + " would like to exercise with you from " + startDate.toString() + " to " + endDate.toString() + ".";

  if (exercise) {
    message = senderName + " would like to do " + exercise.toString() + " with you from " + startDate.toString() + " to " + endDate.toString() + ".";
  }

  if (senderUser.get("FriendsList").indexOf(recipientUserId) === -1) {
  	response.error("The recipient is not the sender's friend, cannot send push");
  }
  // var recipientUser = new Parse.User();
  // recipientUser.id = recipientId;
  var pushQuery = new Parse.Query(Parse.Installation);
  pushQuery.equalTo("facebookId", recipientUserId);

  Parse.Push.send({
  	where: pushQuery,
  	data: {
  		alert: message,
      matchName: matchName,
      exerciseName: exercise,
      matchId: senderId,
      requestStartDate: request.params.startDate,
      requestEndDate: request.params.endDate
  	}
  }).then(function() {
  	response.success("Request was sent successfully to " + recipientUserId);
  }), function(error) {
  	response.error("Push failed to send with error: " + error.message);
  };
});

Parse.Cloud.define("pushResponse", function(request, response) {
  var senderUser = request.user;
  var senderName = request.params.name;
  var recipientUserId = request.params.facebookId;
  var startDate = new Date(request.params.startDate - 25200000);
  var endDate = new Date(request.params.endDate - 25200000);
  var message = senderName + " has confirmed to exercise with you from " + startDate + " to " + endDate + "!";

  if (senderUser.get("FriendsList").indexOf(recipientUserId) === -1) {
    response.error("The recipient: " + recipientUserId + " is not the sender's friend, cannot send push");
  }

  var pushQuery = new Parse.Query(Parse.Installation);
  pushQuery.equalTo("facebookId", recipientUserId);

  Parse.Push.send({
    where: pushQuery,
    data: {
      alert: message
    }
  }).then(function() {
    response.success("Confirmation was sent successfully!");
  }), function(error) {
    response.error("Push failed to send with error: " + error.message);
  };
});