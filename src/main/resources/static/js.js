document.addEventListener('DOMContentLoaded', function () {
    // Add built-in CSS
    const style = document.createElement('style');
    style.textContent = `
        #searchResults {
            position: absolute;
            background-color: white;
            border: 1px solid #ddd;
            border-radius: 5px;
            max-height: 200px;
            overflow-y: auto;
            width: 100%;
            z-index: 1000;
        }

        #searchResults a {
            padding: 10px;
            display: block;
            text-decoration: none;
            color: black;
        }

        #searchResults a:hover, #searchResults a.active {
            background-color: #f1f1f1;
        }
    `;
    document.head.appendChild(style);

    const queryInput = document.getElementById('query');
    const searchResults = document.getElementById('searchResults');
    let currentIndex = -1;

    queryInput.addEventListener('input', function () {
        const query = queryInput.value.trim();

        if (query.length > 0) {
            fetch(`/search/api/search?query=${encodeURIComponent(query)}`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    searchResults.innerHTML = '';
                    currentIndex = -1; // Reset index
                    if (data.length > 0) {
                        data.forEach(client => {
                            const resultItem = document.createElement('a');
                            resultItem.href = `/clients/view?id=${client.id}`;
                            resultItem.textContent = client.phoneNumber ? `${client.searchName} (${client.phoneNumber})` : `${client.searchName}`;
                            resultItem.className = 'list-group-item list-group-item-action'; // Bootstrap styling
                            searchResults.appendChild(resultItem);
                        });
                        searchResults.style.display = 'block';
                    } else {
                        searchResults.style.display = 'none';
                    }
                })
                .catch(error => console.error('Error fetching search results:', error));
        } else {
            searchResults.style.display = 'none';
        }
    });

    queryInput.addEventListener('keydown', function (event) {
        const items = searchResults.querySelectorAll('a');
        if (items.length === 0) return;

        if (event.key === 'ArrowDown') {
            event.preventDefault();
            if (currentIndex < items.length - 1) {
                currentIndex++;
                updateActiveItem(items);
            }
        } else if (event.key === 'ArrowUp') {
            event.preventDefault();
            if (currentIndex > 0) {
                currentIndex--;
                updateActiveItem(items);
            }
        } else if (event.key === 'Enter') {
            event.preventDefault();
            if (currentIndex >= 0 && currentIndex < items.length) {
                items[currentIndex].click();
            }
        }
    });

    function updateActiveItem(items) {
        items.forEach((item, index) => {
            if (index === currentIndex) {
                item.classList.add('active');
                item.scrollIntoView({ block: 'nearest' });
            } else {
                item.classList.remove('active');
            }
        });
    }

    document.addEventListener('click', function (event) {
        if (!searchResults.contains(event.target) && event.target !== queryInput) {
            searchResults.style.display = 'none';
        }
    });
});