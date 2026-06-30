# Verity Voice Mod — NeoForge 1.21.1

**I** tuşuna basınca sohbette `<Verity> mesaj` formatında bir mesaj varsa onu yüksek perdeli sesle okur.

---

## Nasıl Çalışır

1. Oyun çalışırken sohbette `<Verity> merhaba` gibi bir mesaj gelirse mod bunu yakalar.
2. **I** tuşuna bastığında bu mesajı yüksek perdeli (ince) sesle yüksek sesle okur.
3. Sohbette Verity mesajı yoksa ekranda uyarı çıkar, ses çıkmaz.

## Kurulum

1. [NeoForge 1.21.1](https://neoforged.net/) yükle.
2. Derlenmiş `.jar` dosyasını `mods/` klasörüne at.
3. Oyunu başlat.

## Ses Sistemi

Mod, sistemin yerleşik TTS motorunu kullanır:

| İşletim Sistemi | Motor |
|---|---|
| **Windows** | PowerShell `System.Speech` — Kadın sesi, hız: 5 (ince ses) |
| **macOS** | `say -v Samantha -r 230` |
| **Linux** | `espeak-ng -p 90 -s 175` (yükle: `sudo apt install espeak-ng`) |

> **Linux kullanıcıları:** `espeak-ng` yüklü değilse ses çıkmaz. Terminalde `sudo apt install espeak-ng` çalıştır.

## Derleme (Yerel)

```bash
./gradlew build
```

Çıktı: `build/libs/verityvoice-1.21.1-1.0.0.jar`

## GitHub Actions ile Otomatik Derleme

Repo'ya kod push ettiğinde GitHub otomatik olarak modü derler ve `Actions` sekmesinde artifact olarak indirebilirsin.

### Adımlar:
1. Bu klasörü GitHub reposuna yükle.
2. `Actions` sekmesine gir → iş akışı otomatik başlar.
3. Derleme bitince `verity-voice-mod-jar` adlı artifact'ı indir.

## Tuş Bağlama

Varsayılan: **I** tuşu

Değiştirmek için: `Ayarlar → Kontroller → Verity Ses Modu → Verity Mesajını Oku`

## Mod Bilgileri

- **Minecraft:** 1.21.1
- **NeoForge:** 21.1.172+
- **Java:** 21
- **Yalnızca İstemci Tarafı** — sunucuya yükleme gerekmez
