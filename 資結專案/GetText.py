from urllib.parse import urljoin
import requests
from bs4 import BeautifulSoup

def get_all_text_from_website(base_url, output_file='output.txt'):
    try:
        # 使用 requests 库獲取網站 HTML 內容
        response = requests.get(base_url)
        response.raise_for_status()  # 如果發生錯誤，將引發異常

        # 使用 BeautifulSoup 解析 HTML 內容
        soup = BeautifulSoup(response.text, 'html.parser')

        # 提取所有純文字內容
        text_content = soup.get_text()

        # 移除空格
        text_content = ' '.join(text_content.split())

        # 寫入文本到 output.txt 中
        write_text_to_file(text_content, output_file)

        # 提取所有鏈結
        links = soup.find_all('a', href=True)

        # 遞迴訪問鏈結
        for link in links:
            absolute_url = urljoin(base_url, link['href'])
            get_all_text_from_website(absolute_url, output_file)

        return text_content
    except Exception as e:
        return f"Error: {e}"

def write_text_to_file(text, output_file):
    try:
        with open(output_file, 'a', encoding='utf-8') as file:  # 使用 'a' 模式以附加方式寫入
            file.write(text)
        return "文本成功附加到檔案中"
    except Exception as e:
        return f"Error: {e}"

# 輸入你想要獲取的網站的基礎URL
website_url = input("請輸入網站的基礎URL: ")

# 呼叫函數取得整個網站的文本
result = get_all_text_from_website(website_url)

# 印出結果
print(result)
