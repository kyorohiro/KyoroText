/*
 * 描画速度にあまりこだわらない機能向けのSprite表示機能
 * 
 * ゲーム用のは作成中 
 *  以下のような感じになる
 *   毎回 paint(Graphics g) を呼び出す事をやめる。
  * 　    ※Spriteの位置と描画内容を指定すれば、後はNative側で適当に処理してくれる
 *   もちろん OpenGLで描画する
 *   音関連はDynamicSound と 簡易MIDI
 */
package info.kyorohiro.helloworld.display.simple;