function parseProducts() {
  fetch('/products/parse', {
    method: 'POST'
  })
    .then(response => {
      if (response.ok) {
        console.log('Парсер успешно запущен!');
      } else {
        console.error('Ошибка при запуске парсера!');
      }
    })
    .catch(error => {
      console.error('Произошла ошибка:', error);
    });
}
function startBot() {
  fetch('/products/startBot', {
    method: 'POST'
  })
    .then(response => {
      if (response.ok) {
        console.log('Бот успешно запущен!');
      } else {
        console.error('Ошибка при запуске бота!');
      }
    })
    .catch(error => {
      console.error('Произошла ошибка:', error);
    });
}
fetch('/products/sites')
    .then(response => response.json())
    .then(data => {
        const sites = data;
        const siteList = document.getElementById('siteList');
        sites.forEach(site => {
            const a = document.createElement('a');
            a.href = site.url;
            a.textContent = site.name;
            siteList.appendChild(a);
            siteList.appendChild(document.createElement('br'));
        });
    })
    .catch(error => {
        console.error('Произошла ошибка при получении списка сайтов:', error);
    });
