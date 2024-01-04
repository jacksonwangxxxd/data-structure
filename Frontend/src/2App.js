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

// 模擬追蹤清單
function StockWatchlist({ stocks, onStockClick }) {
  const stockItems = stocks.map((stock, index) => (
    <div key={index} className="list-item" onClick={() => onStockClick(stock.name)}>
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
  const [showImage, setShowImage] = useState(false);
  const [stockWatchlist, setStockWatchlist] = useState([
    { name: '台積電', price: ' $' + 579.00, change: -2.36 },
    { name: '聯電', price: ' $' + 50.6, change: -2.50 },
    { name: '鴻海', price: ' $' + 104.6, change: -0.48 },
  ]);

  const handleSearch = async () => {
    try {

      // 模擬搜尋結果
      if (searchTerm == '台積電') {
        var data = {
          "results": [
            { "title": "台積電即時行情", "link": "https://tw.stock.yahoo.com/quote/2330.TW" },
            { "title": "台積電技術分析", "link": "https://tw.tradingview.com/symbols/TWSE-2330/technicals/" },
            { "title": "台積電官方網站", "link": "https://www.tsmc.com/chinese" },
          ]
        };
      } else if (searchTerm == '聯電') {
        var data = {
          "results": [
            { "title": "聯電即時行情", "link": "https://tw.stock.yahoo.com/quote/2303.TW" },
            { "title": "聯電技術分析", "link": "https://tw.tradingview.com/symbols/TWSE-2303/technicals/" },
            { "title": "聯電官方網站", "link": "https://www.umc.com/" },
          ]
        };
      } else if (searchTerm == '鴻海') {
        var data = {
          "results": [
            { "title": "鴻海即時行情", "link": "https://tw.stock.yahoo.com/quote/2317.TW" },
            { "title": "鴻海技術分析", "link": "https://tw.tradingview.com/symbols/TWSE-2317/technicals/" },
            { "title": "鴻海官方網站", "link": "https://www.honhai.com/zh-tw/" },
          ]
        };
      } else if (searchTerm == '永豐金') {
        var data = {
          "results": [
            { "title": "永豐金即時行情", "link": "https://tw.stock.yahoo.com/quote/2890.TW" },
            { "title": "永豐金技術分析", "link": "https://tw.tradingview.com/symbols/TWSE-2890/technicals/" },
            { "title": "永豐金官方網站", "link": "https://www.sinotrade.com.tw/newweb/" },
          ]
        };
      } else if (searchTerm == "電子") {
        setShowImage(searchTerm == "電子")
      }

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
          {showImage && (
            <img
              src={`${process.env.PUBLIC_URL}/cloud.jpg`}
              alt="Your Image"
              className="additional-image"
            />)}
          <StockWatchlist stocks={stockWatchlist} onStockClick={onStockClick} />

        </div>
      </header>
    </div>
  );
}

export default App;
