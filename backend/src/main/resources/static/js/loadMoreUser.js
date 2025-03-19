document.addEventListener("DOMContentLoaded", function () {
  let currentPage = 0; // Página actual
  const loadMoreButton = document.getElementById("load-more-btn");
  const loadingSpinner = document.getElementById("loading-spinner");
  const userList = document.getElementById("user-list");

  if (loadMoreButton) {
    loadMoreButton.addEventListener("click", function () {
      loadingSpinner.style.display = "block";
      loadMoreButton.disabled = true;

      currentPage += 1; // Incrementar la página actual

      fetch(`/admin/users/load?page=${currentPage}`)
        .then((response) => response.text())
        .then((html) => {
          userList.insertAdjacentHTML("beforeend", html);
          loadingSpinner.style.display = "none";
          loadMoreButton.disabled = false;
        })
        .catch((error) => {
          console.error("Error al cargar más usuarios:", error);
          loadingSpinner.style.display = "none";
          loadMoreButton.disabled = false;
        });
    });
  }
});
