function appendMessageToArea(type, senderNickname, message, date, isOne, isAppend, msgId) {
    let chatArea = $("#chatMessageArea");
    let messageTag;

    if (type === "SYSTEM_LINE") {
        messageTag = $("<div>").addClass("read-separator").attr("id", "last-read-marker")
            .append($("<hr>"))
            .append($("<span>").text(message))
            .append($("<hr>"));
    }
    else if (type !== "MESSAGE") {
        // 일반 시스템 메시지 (입장, 퇴장 등)
        messageTag = $("<div>").addClass("system-message")
            .append($("<span>").text(senderNickname + (message || "")));


    } else {
        //일반 채팅
        let isMe = (senderNickname === userNickname); // 내가 보낸 건지 확인
        let groupClass = isMe ? "me" : "opponent";
        let currentTime = getCurrentTime(date);

        // 메인 태그 영역
        messageTag = $("<div>").addClass("message-group").addClass(groupClass);

        if (msgId) messageTag.attr("data-id", msgId); // ID 저장

        // 상대방일 때만 닉네임 표시
        if (!isMe) {
            messageTag.append($("<div>").addClass("sender-name").text(senderNickname));
        }

        // 내용물 생성
        let contentWrapper = $("<div>").addClass("message-content-wrapper");
        let bubble = $("<div>").addClass("message-bubble").text(message);
        let time = $("<span>").addClass("message-time").text(currentTime);

        if (isMe) {
            // 내가 보낸 경우: 시간, 말풍선 순서
            contentWrapper.append(time).append(bubble);
        } else {
            // 상대방이 보낸 경우: 말풍선, 시간 순서
            contentWrapper.append(bubble).append(time);
        }

        messageTag.append(contentWrapper);
    }
    if(isAppend) {
        chatArea.append(messageTag);
    } else {
        chatArea.prepend(messageTag);
    }


    if(isOne) {
        // 화면에 추가 후 스크롤 하단 고정
        chatArea.scrollTop(chatArea[0].scrollHeight);
    }

}


function getCurrentTime(date) {
    let dateString = date ? new Date(date) : new Date();

    return dateString.toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit',
        hour12: true
    });
}


function sendMessage() {
    let input = $("#messageInput");
    let message = input.val();

    if(message.includes("|")) {

        message = replaceMessage(message);
    }

    let messageProtocol = roomId + "|" + "MESSAGE" + "|" + userNickname + "|" + message;

    if (message.trim()) {
        socket.send(messageProtocol);
        input.val('');
        input.focus();
    }
}



function replaceMessage(message) {

    return message.replace("|", "│");
}