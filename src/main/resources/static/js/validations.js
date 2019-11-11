function validateForm() {
  if (document.forms["form"]) {
    let form = document.forms["form"].elements;
    const validationResult = Object.keys(form).map(key => {
      return validations(form, key);
    });
    return validationResult.reduce((cu, ac) => ac && cu)
  }
}

function validations(form, key) {
  //allow backend validation checking
  if (window.location.href.indexOf("disableclientvalidations") !== -1) {
    return true;
  }
  // remove error
  if (document.querySelector(`.error[name=${form[key].name}`)) {
    document.querySelector(`.error[name=${form[key].name}]`).remove()
  }
  const label = document.querySelector(`label[for=${form[key].name}`)
  const error = document.createElement('p');
  error.setAttribute("class", "error");
  error.setAttribute("name", form[key].name);

  // required validation
  if (form[key].value == "" && form[key].type !== "hidden") {
    const errorText = document.createTextNode(`${label.innerText} is required`);
    error.appendChild(errorText);
    form[key].parentNode.insertBefore(error, form[key])
    return false;
  }
  // state validation
  if ((form[key].name === "state" || form[key].name === "mailingState")
    && form[key].value.length < 2) {
    const errorText = document.createTextNode(`${label.innerText} requires 2 characters`);
    error.appendChild(errorText);
    form[key].parentNode.insertBefore(error, form[key])
    return false;
  }
  // zip validation
  if ((form[key].name === "zipCode" || form[key].name === "mailingZipCode")
    && form[key].value.length < 5) {
    const errorText = document.createTextNode(`${label.innerText} requires 5 characters`);
    error.appendChild(errorText);
    form[key].parentNode.insertBefore(error, form[key])
    return false;
  }
  return true;
}

window.onload = () => {
  if (document.forms["form"]) {
    let form = document.forms["form"].elements;
    return Object.keys(form).map(key => {
      form[key].addEventListener("change", (e) => (validations(form, key)));
    });
  }
}