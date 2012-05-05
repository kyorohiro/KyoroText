package info.kyorohiro.helloworld.pdfparser.test;

public class TestData {
	public static String _TEST_INDEX = 
		"<<\n"+
		" /Type /Page\n"+
		" /Parent 7 0 R       % back pointer\n"+
		" /Resources 3 0 R    % font to use\n"+
		" /Contents 2 0 R     % page image\n"+
		">>\n";

	public static String TEST_DATA = 
		"%PDF-1.2\n"+
		"\n"+
		"1 0 obj % page object - page 1\n"+
		"<<\n"+
		" /Type /Page\n"+
		" /Parent 7 0 R       % back pointer\n"+
		" /Resources 3 0 R    % font to use\n"+
		" /Contents 2 0 R     % page image\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"2 0 obj % contents object\n"+
		"<<\n"+
		" /Length 1335    % # of bytes between stream and endstream\n"+
		">>\n"+
		"stream\n"+
		"% draw three lines of text\n"+
		"\n"+
		"BT\n"+
		"/F1 24 Tf            % setfont\n"+
		"1 0 0 1 72 648 Tm    % moveto\n"+
		"(Hello World) Tj     % show\n"+
		"1 0 0 1 72 612 Tm\n"+
		"<4D53835383568362834E3234837C834383938367> Tj % shift-jis string\n"+
		"1 0 0 1 72 576 Tm\n"+
		"0.5 g                % setgray\n"+
		"<82BB82EA82F08A44904682C982B582BD82E082CC> Tj\n"+
		"ET\n"+
		"\n"+
		"% draw filled, dark blue, shougi koma\n"+
		"\n"+
		"q                       % gsave\n"+
		"12 0 0 12 72 360 cm     % concat - translate and scale\n"+
		"0 0 0.5 rg              % setcolor for fill\n"+
		"0 0 m                   % moveto\n"+
		"12 0 l                  % lineto\n"+
		"10 11 l\n"+
		"6 12 l\n"+
		"2 11 l\n"+
		"f                       % close path and fill\n"+
		"Q                       % grestore\n"+
		"\n"+
		"% draw stroked brown egg upright\n"+
		"\n"+
		"q\n"+
		".5 w                    % setlinewidth\n"+
		"12 0 0 12 360 360 cm\n"+
		"0.5 0 0 RG              % setcolor for stroking\n"+
		"0 0 m\n"+
		"8 0 4 12 0 12 c         % curveto\n"+
		"-4 12 -8 0 0 0 c\n"+
		"S                       % stroke\n"+
		"Q\n"+
		"\n"+
		"% draw bitmap - dark green steps\n"+
		"\n"+
		"0 0.5 0 rg               % fill color\n"+
		"144 0 0 144 72 144 cm    % scale 16x16 bits to two-inch square\n"+
		"BI\n"+
		"/W 16       % pixcel width\n"+
		"/H 16       % height\n"+
		"/BPC 1      % bits per component\n"+
		"/F /AHx     % filter = ASCII Hex\n"+
		"/IM true    % imagemask (0 - paint, 1 - transparent)\n"+
		"ID\n"+
		"0FFF\n"+
		"0FFF\n"+
		"0FFF\n"+
		"0FFF\n"+
		"00FF\n"+
		"00FF\n"+
		"00FF\n"+
		"00FF\n"+
		"000F\n"+
		"000F\n"+
		"000F\n"+
		"000F\n"+
		"0000\n"+
		"0000\n"+
		"0000\n"+
		"0000>\n"+
		"EI\n"+
		"endstream\n"+
		"endobj       % end of page stream\n"+
		"\n"+
		"3 0 obj % resource object\n"+
		"<<\n"+
		" /ProcSet [ /PDF /Text ]    % operators to use\n"+
		" /Font << /F1 4 0 R >>      % name the font /F1\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"% Font definition taken from Acrobat 4 PDFWriter (some comments added)\n"+
		"\n"+
		"4 0 obj % base font\n"+
		"<<\n"+
		" /Type /Font\n"+
		" /Subtype /Type0                        % Adobe composite font\n"+
		" /BaseFont /#82l#82r#83S#83V#83b#83N    % MS-Gothic\n"+
		" /DescendantFonts [ 5 0 R ]             % points to actual font\n"+
		" /Encoding /90ms-RKSJ-H                 % Shift-JIS encoding\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"5 0 obj % descendent font\n"+
		"<<\n"+
		" /Type /Font\n"+
		" /Subtype /CIDFontType2                 % TrueType\n"+
		" /BaseFont /#82l#82r#83S#83V#83b#83N    % MS-Gothic\n"+
		" /WinCharSet 128\n"+
		" /FontDescriptor 6 0 R                  % points to metric info\n"+
		" /CIDSystemInfo\n"+
		" <<\n"+
		"  /Registry(Adobe)\n"+
		"  /Ordering(Japan1)\n"+
		"  /Supplement 2\n"+
		" >>\n"+
		" /DW 1000\n"+
		" /W [\n"+
		"  231 389 500\n"+
		"  631 631 500\n"+
		" ]\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"6 0 obj % font metric information\n"+
		"<<\n"+
		" /Type /FontDescriptor\n"+
		" /FontName /#82l#82r#83S#83V#83b#83N\n"+
		" /Flags 39\n"+
		" /FontBBox [ -150 -147 1100 853 ]\n"+
		" /MissingWidth 507\n"+
		" /StemV 92\n"+
		" /StemH 92\n"+
		" /ItalicAngle 0\n"+
		" /CapHeight 853\n"+
		" /XHeight 597\n"+
		" /Ascent 853\n"+
		" /Descent -147\n"+
		" /Leading 0\n"+
		" /MaxWidth 1000\n"+
		" /AvgWidth 507\n"+
		" /Style << /Panose <0805020B0609000000000000> >>\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"% End of font defintion from PDFWriter\n"+
		"\n"+
		"7 0 obj % pages object\n"+
		"<<\n"+
		" /Type /Pages\n"+
		" /Kids [ 1 0 R ]               % list of pages (only one in this case)\n"+
		" /Count 1                      % # of pages - one\n"+
		" /MediaBox [ 0 0 595 842 ]     % A4 portrait\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"8 0 obj % catalog object\n"+
		"<<\n"+
		" /Type /Catalog\n"+
		" /Pages 7 0 R      % points to pages object\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"9 0 obj % info object\n"+
		"<<\n"+
		" /CreationDate (D:19991115)\n"+
		" /Title (Hand-written sample PDF)\n"+
		" /Author (ARAI Bunkichi, Yokohama Koubunsha)\n"+
		">>\n"+
		"endobj\n"+
		"\n"+
		"xref\n"+
		"0 10\n"+
		"0000000000 65535 f\n"+
		"0000000012 00000 n\n"+
		"0000000184 00000 n\n"+
		"0000001672 00000 n\n"+
		"0000001888 00000 n\n"+
		"0000002185 00000 n\n"+
		"0000002569 00000 n\n"+
		"0000002992 00000 n\n"+
		"0000003218 00000 n\n"+
		"0000003324 00000 n\n"+
		"trailer\n"+
		"<<\n"+
		" /Root 8 0 R    % points to catalog object\n"+
		" /Info 9 0 R    % info object\n"+
		" /Size 10       % # of entries in xref\n"+
		">>\n"+
		"startxref\n"+
		"3475\n"+
		"%%EOF\n";
}
