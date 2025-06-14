function convertFileToBase64(inputFile, callback) {
    const file = inputFile.files[0];
    if (!file) {
        callback("");
        return;
    }

    const reader = new FileReader();
    reader.onload = function () {
        // 전체 base64 문자열을 그대로 보냄 (data:image/png;base64,... 포함)
        const base64 = reader.result;
        callback(base64);
    };
    reader.onerror = function () {
        console.error("파일을 base64로 변환하는 중 오류 발생");
        callback("");
    };
    reader.readAsDataURL(file); // 중요! 데이터 URI로 읽음 (MIME 포함)
}
