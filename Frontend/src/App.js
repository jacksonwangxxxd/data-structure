// App.js

import React, { useState } from 'react';
import './App.css';

function SearchResultList({ results }) {
  const resultItems = [];

  for (let i = 0; i < results.length; i++) {
    const result = results[i];
    resultItems.push(
      <div key={i} className="result-item">
        <a href={result.link} target="_blank" rel="noopener noreferrer">
          {result.title}
        </a>
      </div>
    );
  }

  return <div className="results-container">{resultItems}</div>;
}

function App() {
  const [searchTerm, setSearchTerm] = useState('');
  const [results, setResults] = useState([]);

  const handleSearch = async () => {
    try {
      // const response = await fetch(`http://localhost:8080/search?query=${searchTerm}`);
      // const data = await response.json();

      const data = {
        "results": [
          { "title": "聯電官方網站", "link": "https://www.umc.com/" },
          { "title": "聯電股票價格即時行情", "link": "https://tw.stock.yahoo.com/quote/2303.TW" },
          { "title": "聯電技術分析報告", "link": "https://tw.stock.yahoo.com/quote/2303.TW/technical-analysis" },
          // 其他7筆搜尋結果
        ]
      };

      setResults(data.results);
      console.log(data);
    } catch (error) {
      console.error('!Error fetching search results:', error);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Stock Finder</h1>
        <div className="search-container">
          <input
            type="text"
            placeholder="輸入搜尋詞"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <button onClick={handleSearch}>搜尋</button>
        </div>
        <SearchResultList results={results} />
      </header>
    </div>
  );
}

export default App;
