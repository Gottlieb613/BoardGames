//
//  PlayerSelectView.swift
//  Onitama
//
//  Created by Charlie Gottlieb on 6/28/24.
//

import SwiftUI

//var gameMode: Int = 0

struct SettingsView: View {
    
    @ObservedObject var sharedSettings = SharedSettings.shared
//    @State private var soundEnabled: Bool = true
//    @State private var notificationsEnabled: Bool = true
    
    @Environment(\.presentationMode) var presentationMode
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Game Modes")) {
                    Picker("Select Game Mode", selection: $sharedSettings.selectedGameMode) {
                        Text("2 Players").tag("2 Players")
                        Text("Bot").tag("Bot")
                    }
                    .pickerStyle(SegmentedPickerStyle())
                }
                
                
                
//                Section(header: Text("Settings")) {
//                    Toggle(isOn: $soundEnabled) {
//                        Text("Sound")
//                    }
//                    Toggle(isOn: $notificationsEnabled) {
//                        Text("Notifications")
//                    }
//                }
            }
            .navigationTitle("Settings")
        }
    }
}

#Preview {
    SettingsView()
}
