// scripts.js

// Smooth scroll effect for buttons or links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        document.querySelector(this.getAttribute('href')).scrollIntoView({
            behavior: 'smooth'
        });
    });
});

// Confirmation before logout
const logoutLink = document.querySelector('a[href="/logout"]');
if (logoutLink) {
    logoutLink.addEventListener('click', function (e) {
        if (!confirm("Are you sure you want to log out?")) {
            e.preventDefault();
        }
    });
}

// Optional: Dynamic greeting based on the time
const hours = new Date().getHours();
const greeting = document.createElement('p');
if (hours < 12) {
    greeting.textContent = 'Good morning! Welcome to the site.';
} else if (hours < 18) {
    greeting.textContent = 'Good afternoon! Hope you are having a great day.';
} else {
    greeting.textContent = 'Good evening! Relax and enjoy your time here.';
}
document.body.prepend(greeting);
greeting.style.textAlign = "center";
greeting.style.marginBottom = "1rem";
greeting.style.fontSize = "1.2rem";
