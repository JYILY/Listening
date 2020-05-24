import json
import random
import urllib
from urllib import parse
import requests
from bs4 import BeautifulSoup
import os
import base64
import binascii
from Crypto.Cipher import AES

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
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3',
        'accept-encoding': 'gzip, deflate, br',
        'accept-language': 'zh-CN,zh;q=0.9,en;q=0.8',
        'referer': 'https://music.163.com/',
        'upgrade-insecure-requests': '1',
        'user-agent': "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36",
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


def printSong(song):
    print(song['name'])
    print(song['album']['picUrl'])
    print(song['duration'])
    print(song['artists'][0]['name'])
    print(song['id'])
    print(song['mvid'])

def get_mp3_url(song_id):
    href = "https://api.imjad.cn/cloudmusic/?type=song&id={0}&br=320000".format(song_id)
    res = requests.get(href,headers=headers)
    url = json.loads(res.text)['data'][0]['url']
    if url == None :
        url = "http://music.163.com/song/media/outer/url?id={0}.mp3".format(song_id)
    print(url)

def get_mv_url(mv_id):
    url = 'https://music.163.com/mv?id='+str(mv_id)
    UserAgent = random.choice(userAgentList)
    own_headers = {
        'accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9',
        'accept-encoding': 'gzip, deflate, br',
        'accept-language': 'zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6',
        'referer': 'https://music.163.com/',
        'upgrade-insecure-requests': '1',
        'user-agent': UserAgent,
        'sec-fetch-dest': 'iframe',
        'sec-fetch-mode': 'navigate',
        'sec-fetch-site': 'same-origin'
    }
    res = requests.get(url, headers=headers, timeout=5)
    soup = BeautifulSoup(res.text, 'lxml')
    mvUrl = soup.find('meta',property='og:video')['content']
    mvUrl = parse.unquote(mvUrl)
    print(mvUrl)

def get_song_lyrics(song_id):
    url = 'http://music.163.com/api/song/lyric?os=pc&id=%s&lv=-1&kv=-1&tv=-1'%(song_id)
    res = requests.get(url,headers=headers).text
    json_data = json.loads(res)
    if 'nolyric' in json_data.keys():
        print(1)
        print("[00:00.000]纯音乐，请欣赏")
        return 
    lyrics = json_data['lrc']['lyric']
    lyrics = lyrics.split('\n')
    print(len(lyrics))
    for lyric in lyrics:
        print(lyric)

def get_playlist_detail(playlist_id):
    url = 'http://music.163.com/api/playlist/detail?id=' + str(playlist_id)
    new_url = "http://music.163.com/weapi/v3/playlist/detail?id=" + str(playlist_id)
    UserAgent = random.choice(userAgentList)
    own_headers = headers
    own_headers['user-agent'] = UserAgent
    res = requests.get(url,headers=own_headers,timeout=10)
    result = json.loads(res.text)['result']
    songList = result['tracks']
    print(len(songList))
    for song in songList:
        printSong(song)
 
session = requests.Session()

header  = {
    'Accept': '*/*',
    'Accept-Encoding': 'gzip,deflate,sdch',
    'Accept-Language': 'zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4',
    'Connection': 'keep-alive',
    'Content-Type': 'application/x-www-form-urlencoded',
    'Host': 'music.163.com',
    'Referer': 'http://music.163.com/search/',
    'User-Agent':
    'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36'  # NOQA
}
cookies = {'appver': '1.5.2'}
playlist_class_dict = {}
session = requests.Session()

def httpRequest(method, action, query=None, urlencoded=None, callback=None, timeout=None):
        connection = json.loads(rawHttpRequest(method, action, query, urlencoded, callback, timeout))
        return connection

def rawHttpRequest(method, action, query=None, urlencoded=None, callback=None, timeout=None):
        if method == 'GET':
            url = action if query is None else action + '?' + query
            connection = session.get(url)
        elif method == 'POST':
            connection = session.post(action, query, header)
        elif method == 'Login_POST':
            connection = session.post(action,  query, header)
            session.cookies.save()
        connection.encoding = 'UTF-8'
        return connection.text

def aesEncrypt(text, secKey):
    pad = 16 - len(text) % 16
    text = text + chr(pad) * pad
    encryptor = AES.new(secKey, 2, '0102030405060708')
    ciphertext = encryptor.encrypt(text)
    ciphertext = base64.b64encode(ciphertext).decode('utf-8')
    return ciphertext
def rsaEncrypt(text, pubKey, modulus):
    text = text[::-1]
    rs = pow(int(binascii.hexlify(str.encode(text)), 16), int(pubKey, 16), int(modulus, 16))
    return format(rs, 'x').zfill(256)
def createSecretKey(size):
    # print(os.urandom(size))
    return (''.join(map(lambda xx: (hex(xx)[2:]), os.urandom(size))))[0:16]

def get_playlist_detail(playlist_id):
    text = {
        'id': playlist_id,
        'limit':100,
        'total':True,
        'format': 'json'
    }
    text = json.dumps(text)
    nonce = '0CoJUm6Qyw8W8jud'
    pubKey = '010001'
    modulus = ('00e0b509f6259df8642dbc35662901477df22677ec152b5ff68ace615bb7'
       'b725152b3ab17a876aea8a5aa76d2e417629ec4ee341f56135fccf695280'
       '104e0312ecbda92557c93870114af6c9d05c4f7f0c3685b7a46bee255932'
       '575cce10b424d813cfe4875d3e82047b97ddef52741d546b8e289dc6935b'
       '3ece0462db0a22b8e7')
    secKey = createSecretKey(16)
    encText = aesEncrypt(aesEncrypt(text, nonce), secKey)
    encSecKey = rsaEncrypt(secKey, pubKey, modulus)
    data = {
        'params': encText,
        'encSecKey': encSecKey
    }
    action = 'http://music.163.com/weapi/v3/playlist/detail?csrf_token='
    playlistJson = httpRequest('POST', action, data)
    songs = playlistJson['playlist']['tracks']
    print(len(songs))
    for song in songs:
    	print(song['name'])
    	print(song['al']['picUrl'])
    	print(song['dt'])
    	print(song['ar'][0]['name'])
    	print(song['id'])
    	print(song['mv'])

def get_one_page_playlist(page):
    own_userAgent = random.choice(userAgentList)
    own_headers = headers 
    from_data = {"order": "hot","cat": "华语","limit": '32',"offset": str(32*(page-1))}
    url = 'https://music.163.com/discover/playlist/?'+parse.urlencode(from_data)
    res = requests.get(url, headers=headers, data=from_data, timeout=5)
    soup = BeautifulSoup(res.text, 'lxml')
    playlistList = soup.find_all('div', class_='u-cover u-cover-1')
    print(len(playlistList))
    for a in playlistList:
        print(a.find('a',class_='msk')['title'])
        print(a.find('a',class_='msk')['href'].split('=')[1])
        print(a.find('img')['src'])

def get_toplist_page():
    url = 'http://music.163.com/api/toplist/detail?'
    toplists = json.loads(requests.get(url,headers=headers).text)['list']
    print(len(toplists))
    for toplist in toplists :
        print(toplist['name'])
        print(toplist['id'])
        print(toplist['coverImgUrl'])
        
def search(text):
    own_data={'s':text,'limit':'25','offset':0,'type':'1'}
    url = 'http://music.163.com/api/search/pc?'+parse.urlencode(own_data)
    data = json.loads(requests.get(url,headers=headers).text)['result']
    songs = data['songs']
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



    
