//
//  DateUtil.m
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 28/12/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import "DateUtil.h"

@implementation DateUtil

+ (NSDate *) dateFromString: (NSString *) strDate{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"dd/MM/yyyy"];
    if (strDate != nil && ![strDate isEqualToString:@""]) {
        return [formatter dateFromString:strDate];
    }
    
    return nil;
}

+ (NSString *) stringFromDate: (NSDate *) date{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"dd/MM/yyyy"];
    if (date != nil) {
        return [formatter stringFromDate:date];
    }
    
    return nil;
}

+ (NSString *) stringUSFromDate: (NSDate *) date{
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd"];
    if (date != nil) {
        return [formatter stringFromDate:date];
    }
    
    return nil;
}

@end