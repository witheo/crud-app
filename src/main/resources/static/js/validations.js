function validateForm() {
  //remove all errors
  document.querySelectorAll(".error").forEach(el => el.remove());
  let form = document.forms["form"].elements;
  return Object.keys(form).every(key => {
    const label = document.querySelector(`label[for=${form[key].name}`)
    const error = document.createElement('p');
    error.setAttribute("class", "error");

    //required validation
    if (form[key].value == "" && form[key].type !== "hidden" ) {
      const errorText = document.createTextNode(`${label.innerText} is required`);
      error.appendChild(errorText);
      form[key].parentNode.insertBefore(error, form[key])
      return false;
    }
    //state validation
    if ((form[key].name === "state" || form[key].name === "mailingState")
      && form[key].value.length < 2) {
      const errorText = document.createTextNode(`${label.innerText} requires 2 characters`);
      error.appendChild(errorText);
      form[key].parentNode.insertBefore(error, form[key])
      return false;
    }
    //zip validation
    if ((form[key].name === "zipCode" || form[key].name === "mailingZipCode")
      && form[key].value.length < 5) {
      const errorText = document.createTextNode(`${label.innerText} requires 5 characters`);
      error.appendChild(errorText);
      form[key].parentNode.insertBefore(error, form[key])
      return false;
    }
    return true;
  });
}
