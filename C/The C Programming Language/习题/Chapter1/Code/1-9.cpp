// Test.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#define NONBLANK 'a'	//����ѱ���lastc��ʼ��Ϊһ������ķǿո��ַ�

int main()
{
	int c, lastc;	//c��¼��ǰ�����ַ���ASCIIֵ��
					//lastc���¼��ǰһ�������ַ���ASCIIֵ��
	lastc = NONBLANK;
	while ((c = getchar()) != EOF) {
		if (c != ' ' || lastc != ' ') {
			putchar(c);
		}
		lastc = c;
	}

    return 0;
}

