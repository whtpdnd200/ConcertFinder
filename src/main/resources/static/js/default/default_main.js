function getCsrfTokenFromCookie() {

    let xsrfToken = document.cookie.split('; ')
        .find(row => row.startsWith('XSRF-TOKEN='))
        ?.split('=')[1];

    console.log(xsrfToken);

    return xsrfToken;
}