const locationNameInput = document.querySelector('.location-name-input');

const allowedCharsRegex = /[^A-Za-z\d\s\-,.’'&()/]/g;
const duplicateSpacesRegex = /\s{2,}/;

let errorMessageTimer;

locationNameInput.addEventListener('input', function () {
    const inputValue = this.value;

    const hasInvalidChars = allowedCharsRegex.test(inputValue);
    const hasDuplicateSpaces = duplicateSpacesRegex.test(inputValue);

    if (hasInvalidChars) {
        this.value = this.value.replace(allowedCharsRegex, '');
        showErrorMessage(10000);
    }

    if (hasDuplicateSpaces) {
        this.value = this.value.replace(duplicateSpacesRegex, ' ');
    }
});

function showErrorMessage(milliseconds) {
    let errorInputMessage = document.querySelector('.error-input-message');

    errorInputMessage.textContent = 'Allowed characters: A-Z, a-z, 0-9, space, chars: -,.’\'&()/'
    errorInputMessage.style.display = 'block';

    clearTimeout(errorMessageTimer);

    errorMessageTimer = setTimeout(() => {
        errorInputMessage.style.display = 'none';
    }, milliseconds);
}