
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("validatePush", function(request, response) {
  var senderUser = request.user;
  var senderName = request.params.name;
  var recipientUserId = request.params.facebookId;
  var startDate = request.params.startDate;
  var endDate = request.params.endDate;
  var message = senderName + "would like to work out with you from " + startDate + " to " + endDate + ".";

  if (senderUser.get("FriendsList").indexOf(recipientUserId) === -1) {
  	response.error("The recipient is not the sender's friend, cannot send push");
  }
  var recipientUser = new Parse.User();
  recipientUser.id = recipientUserId;
  var pushQuery = new Parse.Query(Parse.Installation);
  pushQuery.equalTo("user", recipientUser);

  Parse.Push.send({
  	where: pushQuery,
  	data: {
  		alert: message
  	}
  }).then(function() {
  	response.success("Request was sent successfully.");
  }), function(error) {
  	response.error("Push failed to send with error: " + error.message);
  };
});