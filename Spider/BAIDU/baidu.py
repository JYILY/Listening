import json
import random
import urllib
from urllib import parse
import requests
from bs4 import BeautifulSoup

headers={
    'Accept': '*/*'
    ,'Accept-Encoding': 'gzip, deflate, br'
    ,'Accept-Language': 'en,zh-CN;q=0.9,zh;q=0.8,en-US;q=0.7,en-GB;q=0.6'
    ,'Cache-Control': 'no-cache'
    ,'Connection': 'keep-alive'
    ,'Host': 'sp0.baidu.com'
    ,'Pragma': 'no-cache'
    ,'Referer': 'https://www.baidu.com/baidu?isource=infinity&iname=baidu&itype=web&tn=02003390_42_hao_pg&ie=utf-8&wd=%E7%94%B5%E5%BD%B1'
    ,'Sec-Fetch-Dest': 'script'
    ,'Sec-Fetch-Mode': 'no-cors'
    ,'Sec-Fetch-Site': 'same-site'
    ,'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36 Edg/81.0.416.64'
}

data ={
    'resource_id': '28286'
    ,'from_mid': '1'
    ,'format': 'json'
    ,'ie': 'utf-8'
    ,'oe': 'utf-8'
    ,'query': '电影'
    ,'sort_key': '16'
    ,'sort_type': '1'
    ,'stat0': ''
    ,'stat1': ''
    ,'stat2': ''
    ,'stat3': ''
    ,'pn': '8'
    ,'rn': '8'
    ,'cb': 'jQuery110209448239699835654_1588041712307'
}

key_map={
    '最热':'16'
    ,'最新':'17'
    ,'好评':'18'
}

movie_style={
    '类型': ['全部','爱情','喜剧' ,'动作' ,'剧情' ,'科幻' ,'恐怖' ,'动画' ,'惊悚' ,'犯罪']

    ,'地区':['全部' ,'大陆' ,'香港' ,'台湾' ,'美国' ,'韩国', '日本' ,'泰国' ,'英国', '其它']

    ,'年代': ['全部', '2020' ,'2019', '2018', '2010-2017', '00年代' ,'90年代' ,'更早']
}

def get_movie_info(sort_key='最热',stat0='',stat1='',stat2='',page=1):
    url = 'https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?'
    own_data = data
    own_data['query'] = '电影'
    own_data['sort_key'] = key_map[sort_key]
    if stat0 == "全部":
        stat0 = '';
    if stat1 == "全部":
        stat1 = '';
    if stat2 == "全部":
        stat2 = '';
    own_data['stat0'] = stat0
    own_data['stat1'] = stat1
    own_data['stat2'] = stat2
    own_data['pn'] = str((page-1)*8)
    url = url + parse.urlencode(data)
    response = requests.get(url,headers=headers)
    print(response.status_code)
    if response.status_code == 200 :
        text = response.text
        json_data = json.loads(text.split('(')[1][:-1])
        movies = json_data['data'][0]['result']
        for movie in movies:
            print(movie['name'])
            print(movie['additional'])
            print(movie['pic_6n_161'])


if __name__ == '__main__':
    parameters = input().split(" ")
    while len(parameters) == 5:
        get_movie_info(parameters[0],parameters[1],parameters[2],parameters[3],int(parameters[4]))
        parameters = input().split(" ")