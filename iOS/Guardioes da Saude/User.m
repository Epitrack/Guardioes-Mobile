//
//  User.m
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 14/10/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import "User.h"

@implementation User

@synthesize nick;
@synthesize email;
@synthesize password;
@synthesize client;
@synthesize dob;
@synthesize gender;
@synthesize app_token;
@synthesize lon;
@synthesize lat;
@synthesize race;
@synthesize platform;
@synthesize type;
@synthesize zip;
@synthesize idUser;
@synthesize picture;
@synthesize user_token;
@synthesize tw;
@synthesize fb;
@synthesize gl;
@synthesize hashtag;
@synthesize household;
@synthesize survey;
@synthesize symptoms;
@synthesize idHousehold;
@synthesize avatar;
@synthesize photo;

+ (User *)getInstance {
    
    static User *getInstance = nil;
    static dispatch_once_t onceToken;
    
    dispatch_once(&onceToken, ^{
        getInstance = [[self alloc] init];
    });
    
    return getInstance;
}

- (id)init{
    
    if (self = [super init]) {
        client = @"api";
        app_token = @"d41d8cd98f00b204e9800998ecf8427e";
        platform = @"ios";
    }
    return self;
}

- (void) setGenderBySegIndex: (long) segIndex{
    if (segIndex == 0) {
        gender = @"M";
    } else if (segIndex == 1) {
        gender = @"F";
    }
}

- (void) setRaceBySegIndex: (long) segIndex{
    if (segIndex == 0) {
        race = @"branco";
    } else if (segIndex == 1) {
        race = @"preto";
    } else if (segIndex == 2) {
        race = @"pardo";
    } else if (segIndex == 3) {
        race = @"amarelo";
    } else if (segIndex == 4) {
        race = @"indigena";
    }
}

- (void) setGenderByString: (NSString *) strGender{
    if ([strGender isEqualToString:@"Masculino"]) {
        gender = @"M";
    } else if ([strGender isEqualToString:@"Feminino"]) {
        gender = @"F";
    }
}
@end