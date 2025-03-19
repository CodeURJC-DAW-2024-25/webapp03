/*!
 * Start Bootstrap - Agency v7.0.12 (https://startbootstrap.com/theme/agency)
 * Copyright 2013-2023 Start Bootstrap
 * Licensed under MIT (https://github.com/StartBootstrap/startbootstrap-agency/blob/master/LICENSE)
 */
//
// Scripts
//

window.addEventListener("DOMContentLoaded", (event) => {
  // Navbar shrink function
  var navbarShrink = function () {
    const navbarCollapsible = document.body.querySelector("#mainNav");
    if (!navbarCollapsible) {
      return;
    }
    if (window.scrollY === 0) {
      navbarCollapsible.classList.remove("navbar-shrink");
    } else {
      navbarCollapsible.classList.add("navbar-shrink");
    }
  };

  // Shrink the navbar
  navbarShrink();

  // Shrink the navbar when page is scrolled
  document.addEventListener("scroll", navbarShrink);

  //  Activate Bootstrap scrollspy on the main nav element
  const mainNav = document.body.querySelector("#mainNav");
  if (mainNav) {
    new bootstrap.ScrollSpy(document.body, {
      target: "#mainNav",
      rootMargin: "0px 0px -40%",
    });
  }

  // Collapse responsive navbar when toggler is visible
  const navbarToggler = document.body.querySelector(".navbar-toggler");
  const responsiveNavItems = [].slice.call(
    document.querySelectorAll("#navbarResponsive .nav-link")
  );
  responsiveNavItems.map(function (responsiveNavItem) {
    responsiveNavItem.addEventListener("click", () => {
      if (window.getComputedStyle(navbarToggler).display !== "none") {
        navbarToggler.click();
      }
    });
  });
});

let currentPage = 0; // Current page

document.getElementById("load-more").addEventListener("click", function () {
  const spinner = document.getElementById("loading-spinner");
  const loadMoreButton = document.getElementById("load-more");

  // Show spinner and hide button
  spinner.style.display = "block";
  loadMoreButton.style.display = "none";

  // Make an AJAX request
  fetch(`/courses/loadMore?page=${currentPage + 1}`)
    .then((response) => response.text())
    .then((html) => {
      // Insert the new courses into the container
      document
        .getElementById("course-container")
        .insertAdjacentHTML("beforeend", html);

      // Increase current page
      currentPage++;

      // Hide the spinner and show the button again
      spinner.style.display = "none";
      loadMoreButton.style.display = "block";
    })
    .catch((error) => {
      console.error("Error al cargar más cursos:", error);
      spinner.style.display = "none";
      loadMoreButton.style.display = "block";
    });
});
