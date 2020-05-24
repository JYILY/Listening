import json
import random
import urllib
from urllib import parse
import requests
from bs4 import BeautifulSoup
import html

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
    'accept': 'application/json, text/javascript, */*; q=0.01'
    ,'accept-encoding': 'gzip, deflate, br'
    ,'accept-language': 'en,zh-CN;q=0.9,zh;q=0.8,en-US;q=0.7,en-GB;q=0.6'
    ,'cookie': 'pgv_pvi=8501295104; ptui_loginuin=1411045825; RK=cjJN257jXT; ptcz=04cded498f03e0bc8d72258d2544bb4fb08b1ef4621c4a5b185ef74055ef2059; pgv_pvid=8416435460; pac_uid=1_1411045825; XWINDEXGREY=0; o_cookie=1411045825; eas_sid=u1c5e8t4v4t228G2g8K8x6Z5Q0; tvfe_boss_uuid=b8193cec19e2234f; pgg_uid=260346029; pgg_appid=101503919; pgg_openid=59EDD97D37B8CA93EDF2D86815633251; pgg_access_token=81F4C8BCDDD7CF2F02BF913BAFECBC4C; pgg_type=1; pgg_user_type=5; ts_refer=www.baidu.com/link; ts_uid=1738839936; psrf_qqopenid=17470971C584349BFCDC8F4D5A4FBE05; psrf_qqrefresh_token=59A9861433493F6AA307E639B64779E4; psrf_qqaccess_token=38AB028D0F7607E28BBCB9A2A05A6A22; psrf_qqunionid=DE95DB1D93C683AA0EBCC1665A713B30; userAction=1; uin=1411045825; qqmusic_key=Q_H_L_2aueUw50e0POG-81bSPSd-RdU354R2VFKEhDIAbODAI0lV3RG-q_pzqqJe-ISY4; psrf_musickey_createtime=1587454857; psrf_access_token_expiresAt=1595230857; qm_keyst=Q_H_L_2aueUw50e0POG-81bSPSd-RdU354R2VFKEhDIAbODAI0lV3RG-q_pzqqJe-ISY4; pgv_si=s7631519744; pgv_info=ssid=s819334428; player_exist=1; qqmusic_fromtag=66; yq_index=0; yplayer_open=0; yqq_stat=0; ts_last=y.qq.com/portal/playlist.html'
    ,'origin': 'https://y.qq.com'
    ,'referer': 'https://y.qq.com/portal/playlist.html'
    ,'sec-fetch-dest': 'empty'
    ,'sec-fetch-site': 'same-site'
    ,'user-agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.113 Safari/537.36 Edg/81.0.416.58'
}

style_info ={
    '语种': ['国语', '英语', '韩语', '粤语', '日语', '小语种', '闽南语', '法语', '拉丁语'], 
    '流派': ['流行', '轻音乐', '摇滚', '民谣', 'R&#38;B', '嘻哈', '电子', '古典', '乡村', '蓝调', ' 爵士', '新世纪', '拉丁', '后摇', '中国传统', '世界音乐'], 
    '主题': ['ACG', '经典', '网络歌曲', '影视', 'KTV热歌', '儿歌', '中国风', '古风', '情歌', '城市', '现场音乐', '背景音乐', '佛教音乐', 'UP主', '乐器', 'MC喊麦', 'DJ'], 
    '心情': ['伤感', '安静', '快乐', '治愈', '励志', '甜蜜', '寂寞', '宣泄', '思念'], 
    '场景': ['睡前', '夜店', '学习', '运动', '开车', '约会', '工作', '旅行', '派对', '婚礼', '咖啡馆', '跳舞', '校园']}
    
style_id={
    '国语':'165'
    ,'英语':'167'
    ,'韩语':'168'
    ,'粤语':'166'
    ,'日语':'169'
    ,'小语种':'170'
    ,'闽南语':'203'
    ,'法语':'204'
    ,'拉丁语':'205'
    ,'流行':'6'
    ,'轻音乐':'15'
    ,'摇滚':'11'
    ,'民谣':'28'
    ,'R&#38;B':'8'
    ,'嘻哈':'153'
    ,'电子':'24'
    ,'古典':'27'
    ,'乡村':'18'
    ,'蓝调':'22'
    ,'爵士':'21'
    ,'新世纪':'164'
    ,'拉丁':'25'
    ,'后摇':'218'
    ,'中国传统':'219'
    ,'世界音乐':'220'
    ,'ACG':'39'
    ,'经典':'136'
    ,'网络歌曲':'146'
    ,'影视':'133'
    ,'KTV热歌':'141'
    ,'儿歌':'131'
    ,'中国风':'145'
    ,'古风':'194'
    ,'情歌':'148'
    ,'城市':'196'
    ,'现场音乐':'197'
    ,'背景音乐':'199'
    ,'佛教音乐':'200'
    ,'UP主':'201'
    ,'乐器':'202'
    ,'MC喊麦':'226'
    ,'DJ':'14'
    ,'伤感':'52'
    ,'安静':'122'
    ,'快乐':'117'
    ,'治愈':'116'
    ,'励志':'125'
    ,'甜蜜':'59'
    ,'寂寞':'55'
    ,'宣泄':'126'
    ,'思念':'68'
    ,'睡前':'78'
    ,'夜店':'102'
    ,'学习':'101'
    ,'运动':'99'
    ,'开车':'85'
    ,'约会':'76'
    ,'工作':'94'
    ,'旅行':'81'
    ,'派对':'103'
    ,'婚礼':'222'
    ,'咖啡馆':'223'
    ,'跳舞':'224'
    ,'校园':'16'}

help_info = '''
        op - 0 :show_help
        op - 1 :get_one_page_playlist(page)
        op - 2 :get_playlist_detail(playlist_id)
        op - 3 :get_mp3_url(song_id)
        op - 4 :get_mv_url(mv_id)
        op - 5 :get_song_lyrics(song_id)
        op - 6 :get_toplist_page()
        op - 7 :search(text)
        op - 8 :show_all_style()
        op - 99 :exit()'''

def printSong(song):
    print(song['songname'])
    imgUrl = "https://y.gtimg.cn/music/photo_new/T002R300x300M000"+song['albummid']+".jpg?max_age=2592000"
    print(song['interval'])
    print(song['singer'][0]['name'])
    print(song['songmid'])
    print(song['media_mid'])

def get_mp3_url(song_id):
    url = "https://u.y.qq.com/cgi-bin/musicu.fcg?format=json&data=%7B%22req_0%22%3A%7B%22module%22%3A%22vkey.GetVkeyServer%22%2C%22method%22%3A%22CgiGetVkey%22%2C%22param%22%3A%7B%22guid%22%3A%22358840384%22%2C%22songmid%22%3A%5B%22{0}%22%5D%2C%22songtype%22%3A%5B0%5D%2C%22uin%22%3A%221443481947%22%2C%22loginflag%22%3A1%2C%22platform%22%3A%2220%22%7D%7D%2C%22comm%22%3A%7B%22uin%22%3A%2218585073516%22%2C%22format%22%3A%22json%22%2C%22ct%22%3A24%2C%22cv%22%3A0%7D%7D".format(song_id)
    filename = json.loads(requests.get(url).text)['req_0']['data']['midurlinfo'][0]['purl']
    if filename == "":
        print("None")
    else :
        print("http://211.97.73.143/amobile.music.tc.qq.com/"+filename)
    
def get_mv_url(mv_id):
    print("None")

def get_song_lyrics(song_id):
    url ="https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg?nobase64=1&callback=MusicJsonCallback_lrc&pcachetime=1494070301711&songmid="+song_id+"&g_tk=5381&jsonpCallback=MusicJsonCallback_lrc&loginUin=0&hostUin=0&format=jsonp&inCharset=utf8&outCharset=utf-8¬ice=0&platform=yqq&needNewCode=0"
    res = requests.get(url,headers=headers).text
    res = res[22:-1]
    data = json.loads(res)
    lyrics = data['lyric'].split('\n')[5:]
    print(len(lyrics))
    for lyric in lyrics :
        print(lyric)

def get_playlist_detail(playlist_id):
    info1 ={
        'type': '1'
        ,'json': '1'
        ,'utf8': '1'
        ,'onlysong': '1'
        ,'new_format': '1'
        ,'disstid': str(playlist_id)
        ,'g_tk_new_20200303': '1889766135'
        ,'g_tk': '1889766135'
        ,'hostUin': '0'
        ,'format': 'json'
        ,'inCharset': 'utf8'
        ,'outCharset': 'utf-8'
        ,'notice': '0'
        ,'platform': 'yqq.json'
        ,'needNewCode': '0'
    }
    info2={
        'g_tk': '5381'
        ,'uin': '0'
        ,'format': 'json'
        ,'inCharset': 'utf-8'
        ,'outCharset': 'utf-8¬ice=0'
        ,'platform': 'h5'
        ,'needNewCode': '1'
        ,'tpl': '3'
        ,'page': 'detail'
        ,'type': 'top'
        ,'topid': '27'
        ,'_': '1519963122923'
    }
    if playlist_id[0] == '-':
        toplist_id = playlist_id[1:]
        info2['topid'] = toplist_id
        url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_cp.fcg?"+parse.urlencode(info2)
        res = requests.get(url,headers=headers)
        json_data = json.loads(res.text)
        songList = json_data['songlist']
        print(len(songList))
        for song in songList:
            print(song['data']['songname'])
            print("https://y.gtimg.cn/music/photo_new/T002R300x300M000"+song['data']['albummid']+".jpg?max_age=2592000")
            print(song['data']['interval']*1000)
            print(song['data']['singer'][0]['name'])
            print(song['data']['songmid'])
            if song['data']['vid']=="":
                print("0")
            else :
                print(song['data']['vid'])
    else:
        url = "https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg?"+parse.urlencode(info1)
        res = requests.get(url,headers=headers)
        json_data = json.loads(res.text)
        songList = json_data['songlist']
        print(len(songList))
        for song in songList:
            print(song['name'])
            print("https://y.gtimg.cn/music/photo_new/T002R300x300M000"+song['album']['mid']+".jpg?max_age=2592000")
            print(song['interval']*1000)
            print(song['singer'][0]['name'])
            print(song['mid'])
            print(song['mv']['id'])

def get_one_page_playlist(page):
    own_headers = headers
    info={
        'picmid': '1'
        ,'rnd': '0.586937298979932'
        ,'g_tk_new_20200303': '1889766135'
        ,'g_tk': '1889766135'
        ,'hostUin': '0'
        ,'format': 'json'
        ,'inCharset':' utf8'
        ,'outCharset': 'utf-8'
        ,'notice': '0'
        ,'platform': 'yqq.json'
        ,'needNewCode': '0'
        ,'categoryId': '6'
        ,'sortId':'4'
        ,'sin': str((page-1)*25)
        ,'ein': str(page*25-1)}
    url = "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?"+parse.urlencode(info)
    res = requests.get(url, headers=own_headers, timeout=5)
    playlists = json.loads(res.text)['data']['list']
    print(len(playlists))
    for playlist in playlists:
        print(playlist['dissname'])
        print(playlist['dissid'])
        print(playlist['imgurl'])

def get_toplist_page():
    url = "https://c.y.qq.com/v8/fcg-bin/fcg_v8_toplist_opt.fcg?page=index&format=html&tpl=macv4&v8debug=1"
    res = requests.get(url,headers=headers).text
    res = res[14:-1]    
    info = json.loads(res)
    toplists = info[0]['List'] + info[1]['List']
    print(len(toplists))
    for toplist in toplists:
        print(toplist['ListName'])
        print("-"+str(toplist['topID']))
        print(toplist['pic'])

def search(text):
    url = 'https://c.y.qq.com/soso/fcgi-bin/client_search_cp?p=1&n=25&format=json&w='+text
    songs = json.loads(requests.get(url).text)['data']['song']['list']
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
        elif op == 8:
            show_all_style()
        

if __name__ == "__main__":
    main()

    
