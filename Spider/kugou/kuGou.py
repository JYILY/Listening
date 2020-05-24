import json
import random
import urllib
from urllib import parse
import requests
from bs4 import BeautifulSoup


userAgentList = [
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 OPR/26.0.1656.60",
    "Opera/8.0 (Windows NT 5.1; U; en)",
    "Mozilla/5.0 (Windows NT 5.1; U; en; rv:1.8.1) Gecko/20061208 Firefox/2.0.0 Opera 9.50",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; en) Opera 9.50",
    "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0",
    "Mozilla/5.0 (X11; U; Linux x86_64; zh-CN; rv:1.9.2.10) Gecko/20100922 Ubuntu/10.10 (maverick) Firefox/3.6.10",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11",
    "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.16 (KHTML, like Gecko) Chrome/10.0.648.133 Safari/534.16",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36",
    "Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/536.11 (KHTML, like Gecko) Chrome/20.0.1132.11 TaoBrowser/2.0 Safari/536.11",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/21.0.1180.71 Safari/537.1 LBBROWSER",
    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; LBBROWSER)",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E; LBBROWSER)",
    "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; .NET4.0C; .NET4.0E; QQBrowser/7.0.3698.400)",
    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; QQDownload 732; .NET4.0C; .NET4.0E)",
    "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.84 Safari/535.11 SE 2.X MetaSr 1.0",
    "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0; SV1; QQDownload 732; .NET4.0C; .NET4.0E; SE 2.X MetaSr 1.0)",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.4.3.4000 Chrome/30.0.1599.101 Safari/537.36",
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 UBrowser/4.0.3214.0 Safari/537.36", ]
    
headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
        'accept-encoding': 'gzip, deflate, br',
        'accept-language': 'en,zh-CN;q=0.9,zh;q=0.8,en-US;q=0.7,en-GB;q=0.6',
        'cache-control': 'max-age=0',
        'sec-fetch-dest': 'document',
        'sec-fetch-mode': 'navigate',
        'cookie': 'kg_mid=241c47c53c7f55223bbfcc26d9fe4e6c; kg_dfid=10PBC34ZZle30qWDcT0RdseO; KuGooRandom=66201586663090599; kg_dfid_collect=d41d8cd98f00b204e9800998ecf8427e; ACK_SERVER_10015=%7B%22list%22%3A%5B%5B%22bjlogin-user.kugou.com%22%5D%5D%7D; Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1587354758,1587372391,1587435445,1588692462; Hm_lpvt_aedee6983d4cfc62f509129360d6bb3d=1588694673'
        ,'sec-fetch-site': 'none',
        'sec-fetch-user': '?1',
        'upgrade-insecure-requests': '1',
        'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.113 Safari/537.36 Edg/81.0.416.58'
}

help_info = '''
        op - 0 :show_help
        op - 1 :get_one_page_playlist(page)
        op - 2 :get_playlist_detail(playlist_id)
        op - 3 :get_mp3_url(song_id)
        op - 4 :get_mv_url(mv_id)
        op - 5 :get_song_lyrics(song_id)
        op - 6 :get_toplist_page()
        op - 7 :search(text)
        op - 99 :exit()'''

def get_mp3_url(song_id):
    url = 'https://www.kugou.com/yy/index.php?r=play/getdata&hash='+song_id
    res = requests.get(url,headers=headers)
    print(json.loads(res.text)['data']['play_url'])
    print(json.loads(requests.get(url,headers=headers).text)['data']['img'])

def get_mv_url(mv_id):
    url = "http://m.kugou.com/app/i/mv.php?cmd=100&hash=%s&ext=mp4"%(mv_id)
    mv = json.loads(requests.get(url,headers=headers).text)['mvdata']
    if mv['rq'] != {}:
        url = mv['rq']['downurl']
    elif mv['sq'] != {}:
        url = mv['sq']['downurl']
    else :
        url = mv['le']['downurl']
    print(url)

def get_song_lyrics(song_id):
    url = "https://www.kugou.com/yy/index.php?r=play/getdata&hash=F0626E36D07F60D68CC99D8E7BCEE812"
    # url = 'https://www.kugou.com/yy/index.php?r=play/getdata&hash='+song_id
    res = requests.get(url,headers = headers).text
    print(res)
    # lyrics = json.loads(requests.get(url).text)['data']['lyrics']
    # lyrics = lyrics.split('\n')[10:]
    # print(len(lyrics))
    # for lyric in lyrics:
    #     print(lyric)

def printSong(song):
    singer,name = song['filename'].split(' - ')[:2]
    print(name)
    print("None")
    print(song['duration'])
    print(singer)
    print(song['hash'])
    mv_id = song['mvhash']
    print( mv_id if mv_id != "" else "0")

def get_playlist_detail(playlist_id):
    if playlist_id[0] =='-':
        playlist_id = playlist_id[1::]
        url ="http://mobilecdn.kugou.com/api/v3/rank/song?pagesize=100&page=1&rankid=%s&json=true"%(playlist_id)
        songs = json.loads(requests.get(url,headers=headers).text)['data']['info']
        print(len(songs))
        for song in songs:
            printSong(song)
    else :
        url = "http://mobilecdn.kugou.com/api/v3/special/song?specialid=%s&json=true"%(str(playlist_id))
        songs = json.loads(requests.get(url,headers=headers).text)['data']['info']
        print(len(songs))
        for song in songs:
            printSong(song)

def get_one_page_playlist(page): 
    url = "http://m.kugou.com/plist/index&page=%d&json=true&tagid=20"%(page)
    playlists = json.loads(requests.get(url).text)['plist']['list']['info'][:25]
    print(len(playlists))
    for playlist in playlists:
        print(playlist['specialname'])
        print(playlist['specialid'])
        print(playlist['imgurl'].replace("{size}","150"))

def get_toplist_page():
    url = 'http://m.kugou.com/rank/list&json=true'
    toplists = json.loads(requests.get(url,headers=headers).text)['rank']['list']
    print(len(toplists))
    for toplist in toplists :
        print(toplist['rankname'])    
        print("-"+str(toplist['rankid']))
        print(toplist['imgurl'].replace("{size}","150"))

def search(text):
    own_data={'format':'json','keyword':text,'page':1,'pagesize':25,'showtype':'1'}
    url = 'http://mobilecdn.kugou.com/api/v3/search/song?'+parse.urlencode(own_data)
    songs = json.loads(requests.get(url,headers).text)['data']['info']
    print(len(songs))
    for song in songs :
        printSong(song)

def show_help():
    print(help_info)

def main():
    op = 1025
    while op != 99:
        op = int(input())
        if op == 0:
            show_help()
        elif op == 1:
            page = input()
            page = int(page)
            get_one_page_playlist(page)
        elif op == 2:
            playlist_id = input()
            get_playlist_detail(playlist_id)
        elif op == 3:
            song_id = input()
            get_mp3_url(song_id)
        elif op == 4:
            mv_id = input()
            get_mv_url(mv_id)
        elif op == 5:
            song_id = input()
            get_song_lyrics(song_id)
        elif op == 6:
            get_toplist_page()
        elif op == 7:
            text= input()
            search(text)
        
if __name__ == "__main__":
    main()


    
