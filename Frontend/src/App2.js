// App.js

import React, { useState } from 'react';
import './App.css';

function SearchResultList({ results }) {
  const resultItems = results.map((result, index) => (
    <div key={index} className="result-item">
      <a href={result.link} target="_blank" rel="noopener noreferrer">
        {result.title}
      </a>
    </div>
  ));

  return <div className="list-container results-container">{resultItems}</div>;
}

function StockWatchlist() {
  const [stockWatchlist, setStockWatchlist] = useState([]);

  useEffect(() => {
    fetchStockData();
  }, []);

  const fetchStockData = async () => {
    try {
      const response = await fetch('http://localhost:8080/getStock');
      const data = await response.json();

      setStockWatchlist(data);
    } catch (error) {
      console.error('Error fetching stock data:', error);
    }
  };

  const stockItems = stockWatchlist.map((stock, index) => (
    <div key={index} className="list-item">
      <span className="stock-name">{stock.name}</span>
      <span className="stock-price">{stock.price}</span>
      <span className={`stock-change ${stock.change > 0 ? 'positive' : 'negative'}`}>
        {stock.change > 0 ? '+' : ''}{stock.change}%
      </span>
    </div>
  ));

  return <div className="list-container">{stockItems}</div>;
}

function App() {
  const [searchTerm, setSearchTerm] = useState('');
  const [results, setResults] = useState([]);
  const [stockWatchlist, setStockWatchlist] = useState([]);

  const handleSearch = async () => {
    try {
      const response = await fetch(`http://localhost:8080/search?query=${query}`);
      const data = await response.json();
      setResults(data.results);
      console.log(data);
    } catch (error) {
      console.error('!Error fetching search results:', error);
    }
  };

  const onStockClick = (stockName) => {
    setSearchTerm(stockName);
    handleSearch();
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
        <div className="main-container">
          <SearchResultList results={results} />
          <StockWatchlist stocks={stockWatchlist} onStockClick={onStockClick} />

        </div>
      </header>
    </div>
  );
}

export default App;
