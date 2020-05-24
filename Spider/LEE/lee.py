import json
import random
import urllib
from urllib import parse
import requests
from bs4 import BeautifulSoup

headers={
        'Connection': 'keep-alive'
        ,'Upgrade-Insecure-Requests': '1'
        ,'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36'
        ,'Sec-Fetch-Mode': 'nested-navigate'
        ,'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3'
        ,'Sec-Fetch-Site': 'same-origin'
        ,'Referer': 'https://ler.app/?wd=Get+It+Beauty+2020'
        ,'Accept-Encoding': 'gzip, deflate, br'
        ,'Accept-Language': 'zh-CN,zh;q=0.9,en;q=0.8'
        ,'Cookie': 'live_num=; 040238=24; Hm_lvt_907ccdabd200c723d53f05a1d0a51eda=1587436413; 160158=0; list_num=0; Hm_lpvt_907ccdabd200c723d53f05a1d0a51eda=1587436946'
}

help_info = """
        0  - show_help()
        1  - search(text)
        2  - getSrc(id)
        99 - exit()
    """
def getSrc(id_):
    url = "https://ler.app/api.php?flag="+id_[0]+"&id="+id_[1:]
    res = requests.get(url).text.split('(')[1][:-2]
    videos = json.loads(res)['info'][0]['video']
    for video in videos :
        print(video.split('$')[1])

def search(text):
    url = "https://ler.app/api.php?wd="+parse.quote(text)
    res = requests.get(url).text
    data = json.loads(res[1:-2])
    if data['success'] == 1:
        data = data['info']
    else :
        data = []
        print("None")
    for item in data :
        print(parse.unquote(item['title']))
        print(str(item['flag'])+item['id'])

def show_help():
    print(help_info)

def main():
    op = 1025
    while op != 99:
        op = int(input())
        if op == 0:
            show_help()
        elif op == 1:
            text = input()
            search(text)
        elif op == 2:
            id_ = input()
            getSrc(id_)

if __name__ =='__main__':
    main()
