#include "ListApp.h"

SqlList createList(int length) 
{
    struct SqlList l;

    for (int i = 0; i < length; i++) {
        l.data[i] = i;
    }
    l.length = length;
    return l;
}

int findElem(SqlList l, int item) 
{
    int index = -1;
    for (int i = 0; i < l.length; i++) {
        if (item == l.data[i]) {
            // ����Ǵ�1��ʼ��i�Ǵ�0��ʼ
            index = i + 1;
            break;
        }
    }
    return index;
}